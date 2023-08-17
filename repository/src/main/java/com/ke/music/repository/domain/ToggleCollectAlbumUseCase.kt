package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ToggleCollectAlbumUseCase @Inject constructor(
    private val albumRepository: com.ke.music.common.repository.AlbumRepository,
    private val httpService: HttpService,
) :
    UseCase<Pair<Long, Boolean>, Unit>(Dispatchers.IO) {


    override suspend fun execute(parameters: Pair<Long, Boolean>) {
        albumRepository.toggleCollectAlbum(parameters.first, parameters.second)

        httpService.collectAlbum(parameters.first, if (parameters.second) 1 else 0)

    }

}