package com.ke.music.repository

import com.ke.music.api.response.PlaylistDynamicResponse
import com.ke.music.api.response.PlaylistTopResponse
import com.ke.music.api.response.Song
import com.ke.music.common.entity.IPlaylist
import com.ke.music.common.repository.CurrentUserRepository
import com.ke.music.common.repository.PlaylistRepository
import com.ke.music.common.repository.SongRepository
import com.ke.music.common.repository.UserRepository
import com.ke.music.room.db.dao.PlaylistDao
import com.ke.music.room.db.dao.PlaylistMusicCrossRefDao
import com.ke.music.room.db.dao.PlaylistSubscriberCrossRefDao
import com.ke.music.room.db.dao.TopPlaylistDao
import com.ke.music.room.db.dao.UserDao
import com.ke.music.room.db.dao.UserPlaylistCrossRefDao
import com.ke.music.room.db.entity.Playlist
import com.ke.music.room.db.entity.PlaylistMusicCrossRef
import com.ke.music.room.db.entity.PlaylistSubscriberCrossRef
import com.ke.music.room.db.entity.TopPlaylist
import com.ke.music.room.db.entity.User
import com.ke.music.room.db.entity.UserPlaylistCrossRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRepository @Inject constructor(
    private val playlistDao: PlaylistDao,
    private val topPlaylistDao: TopPlaylistDao,
    private val playlistSubscriberCrossRefDao: PlaylistSubscriberCrossRefDao,
    private val userIdRepository: CurrentUserRepository,
    private val userDao: UserDao,
    private val userPlaylistCrossRefDao: UserPlaylistCrossRefDao,
    private val songRepository: SongRepository,
    private val playlistMusicCrossRefDao: PlaylistMusicCrossRefDao,
    private val userRepository: UserRepository,
) : PlaylistRepository {


    override suspend fun savePlaylistDetail(
        playlist: com.ke.music.api.response.Playlist,
        songs: List<Song>,
        dynamic: PlaylistDynamicResponse,
    ) {
        //保存歌曲到room
        songRepository.saveSongs(songs)

        val playlistMusicCrossRefList = mutableListOf<PlaylistMusicCrossRef>()
        songs.forEachIndexed { index, song ->
            playlistMusicCrossRefList.add(
                PlaylistMusicCrossRef(
                    playlist.id, song.id, index
                )
            )
        }
        //保存歌单歌曲关联记录到room
        playlistMusicCrossRefDao.insertAll(playlistMusicCrossRefList)

        //保存歌单订阅者到room
        userRepository.saveUsers(playlist.subscribers + playlist.creator)

//        userRepository.saveUsers(
//            listOf(
//                playlist.creator
//            )
//        )

        //保存歌单和用户关系到数据库
        playlistSubscriberCrossRefDao.insertAll(
            playlist.subscribers.mapIndexed { index, user ->
                PlaylistSubscriberCrossRef(playlist.id, user.userId, index)
            }
        )
        savePlaylist(
            playlist.convert(
                shareCount = dynamic.shareCount,
                bookedCount = dynamic.bookedCount,
                commentCount = dynamic.commentCount
            )
        )

        val userId = userIdRepository.userId()

        if (dynamic.subscribed) {
            playlistSubscriberCrossRefDao.insert(
                PlaylistSubscriberCrossRef(
                    playlist.id, userId, 0
                )
            )
        } else {
            playlistSubscriberCrossRefDao.deleteByPlaylistIdAndUserId(
                playlist.id,
                userId
            )
        }
    }

    override suspend fun saveCurrentUserPlaylist(list: List<com.ke.music.api.response.Playlist>) {

        val userId = userIdRepository.userId()
        val userPlaylist = mutableListOf<com.ke.music.api.response.Playlist>()
        val userFollowingPlaylist = mutableListOf<com.ke.music.api.response.Playlist>()
        val userList = mutableListOf<User>()

        list.forEach { playlist ->
            userList.add(playlist.creator.convert())
            if (playlist.creator.userId == userId) {
                //用户创建的
                userPlaylist.add(playlist)
            } else {
                //用户订阅的
                userFollowingPlaylist.add(playlist)
            }
        }

        playlistDao.insertBeforeCheck(
            list.map { it.convert() }
        )

        userDao.insertAll(userList)

        userPlaylistCrossRefDao.insertAll(userPlaylist.mapIndexed { index, playlist ->
            UserPlaylistCrossRef(userId, playlist.id, index)
        })

        playlistSubscriberCrossRefDao.insertAll(
            userFollowingPlaylist.mapIndexed { index, playlist ->
                PlaylistSubscriberCrossRef(playlist.id, userId, index)
            }

        )
    }

    suspend fun savePlaylist(playlist: Playlist) {
        playlistDao.insert(playlist)
    }

    private suspend fun savePlaylistList(list: List<Playlist>) {
        playlistDao.insertAll(list)
    }

    /**
     * 根据id查找歌单
     */
    override fun findById(id: Long) = playlistDao.findById(id)

    override suspend fun findPlaylistById(id: Long): IPlaylist? =
        playlistDao.findPlaylistById(id)

    /**
     * 判断用户有没有关注歌单
     */
    override fun checkUserHasSubscribePlaylist(userId: Long, playlistId: Long): Flow<Boolean> {
        return playlistSubscriberCrossRefDao.findByUserIdAndPlaylistId(userId, playlistId)
            .map {
                it != null
            }
    }

    override fun getCurrentUserPlaylist(containFollowed: Boolean): Flow<List<IPlaylist>> {
        val userId = userIdRepository.userId


        return if (containFollowed) {
            playlistDao.getUserCreatedPlaylist(userId).combine(
                playlistDao.getUserFollowingPlaylist(userId)
            ) { list1, list2 ->
                list1 + list2
            }

        } else {
            playlistDao.getUserCreatedPlaylist(userId)
        }

    }

    /**
     * 获取当前用户创建的歌单
     */
    fun getCurrentUserPlaylist(): Flow<List<IPlaylist>> {
        val userId = userIdRepository.userId
        return playlistDao.getUserCreatedPlaylist(userId)
    }

    /**
     * 当前用户删除自己的歌单
     */
    override suspend fun currentUserDeleteSelfPlaylist(playlistId: Long) {
        playlistDao.deleteByPlaylistId(playlistId)
    }

    /**
     * 当前用户删除关注的歌单
     */
    override suspend fun currentUserCancelFollowingPlaylist(playlistId: Long) {
        val userId = userIdRepository.userId
        playlistSubscriberCrossRefDao.deleteByPlaylistIdAndUserId(playlistId, userId)
    }

    override suspend fun saveTopPlaylists(response: PlaylistTopResponse, deleteOld: Boolean) {
        savePlaylistList(response.playlists.map {
            it.convert()
        })
        val list = response.playlists
            .map {
                TopPlaylist(0, it.id, response.category)
            }
        if (deleteOld) {

            topPlaylistDao.deleteOldAndInsertNew(
                response.category,
                list
            )
        } else {
            topPlaylistDao.insertAll(list)
        }
    }


    override fun topPlaylist(category: String?) = topPlaylistDao.queryByCategory(category)

    suspend fun deleteTopPlaylistsByCategory(category: String?) =
        topPlaylistDao.deleteByCategory(category)


    override suspend fun toggleFollowPlaylist(playlistId: Long): Boolean {

        val userId = userIdRepository.userId()
        val record =
            playlistSubscriberCrossRefDao.getByUserIdAndPlaylistId(userId, playlistId)

        return if (record == null) {
            //收藏歌单
            playlistSubscriberCrossRefDao.insert(
                PlaylistSubscriberCrossRef(
                    playlistId, userId,
                    0
                )
            )
            true
        } else {
            //删除收藏
            playlistSubscriberCrossRefDao.deleteByPlaylistIdAndUserId(
                playlistId, userId
            )
            false
        }
    }
}