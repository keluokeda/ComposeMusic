package com.ke.compose.music.ui.playlist_detail

import android.content.Context
import com.ke.compose.music.convert
import com.ke.compose.music.db.dao.PlaylistMusicCrossRefDao
import com.ke.compose.music.db.dao.PlaylistSubscriberCrossRefDao
import com.ke.compose.music.db.entity.PlaylistMusicCrossRef
import com.ke.compose.music.db.entity.PlaylistSubscriberCrossRef
import com.ke.compose.music.domain.UseCase
import com.ke.compose.music.getUserId
import com.ke.compose.music.repository.MusicRepository
import com.ke.compose.music.repository.PlaylistRepository
import com.ke.compose.music.repository.UserRepository
import com.ke.music.api.HttpService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoadPlaylistDetailUseCase @Inject constructor(
    private val httpService: HttpService,
    private val musicRepository: MusicRepository,
    private val playlistMusicCrossRefDao: PlaylistMusicCrossRefDao,
    private val userRepository: UserRepository,
    private val playlistSubscriberCrossRefDao: PlaylistSubscriberCrossRefDao,
    private val playlistRepository: PlaylistRepository,
    @ApplicationContext private val context: Context
) :
    UseCase<Long, Unit>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long) {

        return withContext(Dispatchers.IO) {

            val playlist =
                httpService.getPlaylistDetail(parameters).playlist
            val songs =
                httpService.getPlaylistTracks(parameters).songs


            val dynamic =
                httpService.getPlaylistDetailDynamic(parameters)


            //保存歌曲到room
            musicRepository.saveMusicListToRoom(songs)

            val playlistMusicCrossRefList = mutableListOf<PlaylistMusicCrossRef>()
            songs.forEachIndexed { index, song ->
                playlistMusicCrossRefList.add(
                    PlaylistMusicCrossRef(
                        parameters, song.id, index
                    )
                )
            }
            //保存歌单歌曲关联记录到room
            playlistMusicCrossRefDao.insertAll(playlistMusicCrossRefList)

            //保存歌单订阅者到room
            userRepository.saveUsers(playlist.subscribers)

            userRepository.saveUsers(
                listOf(
                    playlist.creator
                )
            )

            //保存歌单和用户关系到数据库
            playlistSubscriberCrossRefDao.insertAll(
                playlist.subscribers.mapIndexed { index, user ->
                    PlaylistSubscriberCrossRef(parameters, user.userId, index)
                }
            )

            playlistRepository.savePlaylist(
                playlist.convert(
                    shareCount = dynamic.shareCount,
                    bookedCount = dynamic.bookedCount,
                    commentCount = dynamic.commentCount
                )
            )


            if (dynamic.subscribed) {
                playlistSubscriberCrossRefDao.insert(
                    PlaylistSubscriberCrossRef(
                        parameters, context.getUserId(), 0
                    )
                )
            } else {
                playlistSubscriberCrossRefDao.deleteByPlaylistIdAndUserId(
                    parameters,
                    context.getUserId()
                )
            }

        }
    }
}