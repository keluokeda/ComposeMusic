package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.repository.UserIdRepository
import com.ke.music.room.db.dao.PlaylistSubscriberCrossRefDao
import com.ke.music.room.db.entity.PlaylistSubscriberCrossRef
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