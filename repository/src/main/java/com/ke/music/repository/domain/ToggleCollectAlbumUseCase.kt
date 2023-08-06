package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.repository.AlbumRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ToggleCollectAlbumUseCase @Inject constructor(
    private val albumRepository: AlbumRepository,
    private val httpService: HttpService
) :
    UseCase<Pair<Long, Boolean>, Unit>(Dispatchers.IO) {


    override suspend fun execute(parameters: Pair<Long, Boolean>) {
        albumRepository.toggleCollectAlbum(parameters.first, parameters.second)

        httpService.collectAlbum(parameters.first, if (parameters.second) 1 else 0)

    }

}