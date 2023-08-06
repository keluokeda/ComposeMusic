package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.api.response.Song
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetAlbumSongsUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Long, List<Song>>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long): List<Song> {
        return httpService.getAlbumDetail(parameters).songs
    }
}