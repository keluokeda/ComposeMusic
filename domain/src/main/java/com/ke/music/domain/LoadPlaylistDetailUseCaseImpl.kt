package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.LoadPlaylistDetailUseCase
import com.ke.music.common.repository.PlaylistRepository
import javax.inject.Inject

internal class LoadPlaylistDetailUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val playlistRepository: PlaylistRepository,
) : UseCase<Long, Unit>(), LoadPlaylistDetailUseCase {

    override suspend fun execute(parameters: Long) {
        val playlist =
            httpService.getPlaylistDetail(parameters).playlist
        val songs =
            httpService.getPlaylistTracks(parameters).songs


        val dynamic =
            httpService.getPlaylistDetailDynamic(parameters)

        playlistRepository.savePlaylistDetail(playlist, songs, dynamic)

    }
}