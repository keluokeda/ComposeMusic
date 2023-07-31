package com.ke.compose.music.domain

import com.ke.music.api.HttpService
import com.ke.music.api.response.Song
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetRecommendSongsUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Unit, List<Song>>(Dispatchers.IO) {

    override suspend fun execute(parameters: Unit): List<Song> {
        return httpService.recommendSongs().data!!.dailySongs
    }
}