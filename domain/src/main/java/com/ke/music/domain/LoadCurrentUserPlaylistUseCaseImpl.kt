package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.LoadCurrentUserPlaylistUseCase
import com.ke.music.common.repository.CurrentUserRepository
import com.ke.music.common.repository.PlaylistRepository
import javax.inject.Inject

class LoadCurrentUserPlaylistUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val playlistRepository: PlaylistRepository,
    private val currentUserRepository: CurrentUserRepository,
) : UseCase<Unit, Unit>(), LoadCurrentUserPlaylistUseCase {

    override suspend fun execute(parameters: Unit) {
        val userId = currentUserRepository.userId()
        val list = httpService.getUserPlaylistList(userId).playlist
        playlistRepository.saveCurrentUserPlaylist(list)
    }
}