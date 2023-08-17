package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.GetSongUrlUseCase
import javax.inject.Inject

internal class GetSongUrlUseCaseImpl @Inject constructor(private val httpService: HttpService) :
    UseCase<Long, String>(), GetSongUrlUseCase {

    override suspend fun execute(parameters: Long): String {
        return httpService.getSongUrl(parameters).data.first().url!!
    }
}