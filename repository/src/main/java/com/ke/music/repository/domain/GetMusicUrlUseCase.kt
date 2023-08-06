package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetMusicUrlUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Long, String>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long): String {
//        val url = httpService.getSongDownloadUrl(parameters).data?.url
//
//        if (url != null) {
//            return url
//        }
        return httpService.getSongUrl(parameters).data.first().url!!
    }


}