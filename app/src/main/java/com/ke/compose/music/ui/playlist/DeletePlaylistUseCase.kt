package com.ke.compose.music.ui.playlist

import com.ke.compose.music.domain.UseCase
import com.ke.compose.music.repository.PlaylistRepository
import com.ke.compose.music.repository.UserIdRepository
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * 删除歌单
 */
class DeletePlaylistUseCase @Inject constructor(
    private val httpService: HttpService,
    private val playlistRepository: PlaylistRepository,
    private val userIdRepository: UserIdRepository,
) :
    UseCase<Long, Unit>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long) {


        val playlistCreatorId = playlistRepository.findPlaylistById(parameters)?.creatorId

        val currentUserId = userIdRepository.userId()

        if (currentUserId == playlistCreatorId) {
            //删除自己创建的歌单
            playlistRepository.currentUserDeleteSelfPlaylist(parameters)
            httpService.deletePlaylist(parameters)
        } else {
            //取消关注已关注的歌单
            playlistRepository.currentUserDeleteFollowingPlaylist(parameters)
            httpService.subscribePlaylist(parameters, 2)
        }


    }
}