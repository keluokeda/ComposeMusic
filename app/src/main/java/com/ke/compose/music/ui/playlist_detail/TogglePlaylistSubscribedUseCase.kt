package com.ke.compose.music.ui.playlist_detail

import com.ke.compose.music.db.dao.PlaylistSubscriberCrossRefDao
import com.ke.compose.music.db.entity.PlaylistSubscriberCrossRef
import com.ke.compose.music.domain.UseCase
import com.ke.compose.music.repository.UserIdRepository
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * 切换歌单收藏状态
 */
class TogglePlaylistSubscribedUseCase @Inject constructor(
    private val httpService: HttpService,
    private val playlistSubscriberCrossRefDao: PlaylistSubscriberCrossRefDao,
    private val userIdRepository: UserIdRepository
) :
    UseCase<Pair<Long, Boolean>, Unit>(Dispatchers.IO) {

    override suspend fun execute(parameters: Pair<Long, Boolean>) {

        if (parameters.second) {
            //收藏歌单
            playlistSubscriberCrossRefDao.insert(
                PlaylistSubscriberCrossRef(
                    parameters.first,
                    userIdRepository.userId(),
                    0
                )
            )
        } else {
            //删除收藏
            playlistSubscriberCrossRefDao.deleteByPlaylistIdAndUserId(
                parameters.first,
                userIdRepository.userId()
            )
        }

        httpService.subscribePlaylist(
            parameters.first,
            if (parameters.second) 1 else 2
        )
    }
}