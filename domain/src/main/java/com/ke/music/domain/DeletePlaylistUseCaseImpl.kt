package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.DeletePlaylistUseCase
import com.ke.music.common.repository.CurrentUserRepository
import com.ke.music.common.repository.PlaylistRepository
import javax.inject.Inject

class DeletePlaylistUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val currentUserRepository: CurrentUserRepository,
    private val playlistRepository: PlaylistRepository,
) : UseCase<Long, Unit>(), DeletePlaylistUseCase {
    override suspend fun execute(parameters: Long) {
        val playlistCreatorId = playlistRepository.findPlaylistById(parameters)?.creatorId

        val currentUserId = currentUserRepository.userId()

        if (currentUserId == playlistCreatorId) {
            //删除自己创建的歌单
            playlistRepository.currentUserDeleteSelfPlaylist(parameters)
            httpService.deletePlaylist(parameters)
        } else {
            //取消关注已关注的歌单
            playlistRepository.currentUserCancelFollowingPlaylist(parameters)
            httpService.subscribePlaylist(parameters, 2)
        }
    }
}