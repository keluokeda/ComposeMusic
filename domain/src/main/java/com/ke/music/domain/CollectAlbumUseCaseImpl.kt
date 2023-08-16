package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.CollectAlbumUseCase
import com.ke.music.common.repository.AlbumRepository
import javax.inject.Inject

class CollectAlbumUseCaseImpl @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val httpService: HttpService,
) : UseCase<Pair<Long, Boolean>, Unit>(), CollectAlbumUseCase {

    override suspend fun execute(parameters: Pair<Long, Boolean>) {
        albumRepository.toggleCollectAlbum(parameters.first, parameters.second)

        httpService.collectAlbum(parameters.first, if (parameters.second) 1 else 0)
    }
}