package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.repository.MvRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LoadArtistMvsUseCase @Inject constructor(
    private val httpService: HttpService,
    private val mvRepository: MvRepository
) : UseCase<Long, Unit>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long) {
        val response = httpService.getArtistMv(parameters)

        mvRepository.saveArtistMv(parameters, response.mvs)
    }
}