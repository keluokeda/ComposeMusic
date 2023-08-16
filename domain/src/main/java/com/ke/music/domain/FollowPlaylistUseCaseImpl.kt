package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.FollowPlaylistUseCase
import com.ke.music.common.repository.PlaylistRepository
import javax.inject.Inject

class FollowPlaylistUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val playlistRepository: PlaylistRepository,
) :
    UseCase<Long, Unit>(), FollowPlaylistUseCase {

    override suspend fun execute(parameters: Long) {
        val newState = playlistRepository.toggleFollowPlaylist(parameters)
        httpService.subscribePlaylist(
            parameters,
            if (newState) 1 else 2
        )
    }
}