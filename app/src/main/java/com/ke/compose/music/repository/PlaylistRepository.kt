package com.ke.compose.music.repository

import com.ke.compose.music.db.dao.PlaylistDao
import com.ke.compose.music.db.dao.PlaylistSubscriberCrossRefDao
import com.ke.compose.music.db.entity.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaylistRepository @Inject constructor(
    private val playlistDao: PlaylistDao,
    private val playlistSubscriberCrossRefDao: PlaylistSubscriberCrossRefDao,
    private val userIdRepository: UserIdRepository
) {

    suspend fun savePlaylist(playlist: Playlist) {
        playlistDao.insert(playlist)
    }

    suspend fun savePlaylistList(list: List<Playlist>) {
        playlistDao.insertAll(list)
    }

    /**
     * 根据id查找歌单
     */
    fun findById(playlistId: Long): Flow<Playlist?> = playlistDao.findById(playlistId)

    suspend fun findPlaylistById(playlistId: Long) = playlistDao.findPlaylistById(playlistId)

    /**
     * 判断用户有没有关注歌单
     */
    fun checkUserHasSubscribePlaylist(userId: Long, playlistId: Long): Flow<Boolean> {
        return playlistSubscriberCrossRefDao.findByUserIdAndPlaylistId(userId, playlistId)
            .map {
                it != null
            }
    }

    /**
     * 获取当前用户创建的歌单
     */
    fun getCurrentUserPlaylist(): Flow<List<Playlist>> {
        val userId = userIdRepository.userId

        return playlistDao.getUserCreatedPlaylist(userId)
    }

    /**
     * 当前用户删除自己的歌单
     */
    suspend fun currentUserDeleteSelfPlaylist(playlistId: Long) {
        playlistDao.deleteByPlaylistId(playlistId)
    }

    /**
     * 当前用户删除关注的歌单
     */
    suspend fun currentUserDeleteFollowingPlaylist(playlistId: Long) {
        val userId = userIdRepository.userId
        playlistSubscriberCrossRefDao.deleteByPlaylistIdAndUserId(playlistId, userId)
    }
}