package com.ke.compose.music.ui.playlist_new

import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CreatePlaylistUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Pair<String, Boolean>, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: Pair<String, Boolean>): Boolean {
        return httpService.createPlaylist(parameters.first).code == 200
    }
}