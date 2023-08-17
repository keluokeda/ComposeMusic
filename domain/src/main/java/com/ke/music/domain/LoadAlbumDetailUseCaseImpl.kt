package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.LoadAlbumDetailUseCase
import com.ke.music.common.repository.AlbumRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

internal class LoadAlbumDetailUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val albumRepository: AlbumRepository,
) : UseCase<Long, Unit>(Dispatchers.IO), LoadAlbumDetailUseCase {

    override suspend fun execute(parameters: Long) {
        val response = httpService.getAlbumDetail(parameters)

        val isSub = httpService.getAlbumDynamic(parameters).isSub

        albumRepository.saveAlbumResponseToDatabase(response, isSub)
    }
}