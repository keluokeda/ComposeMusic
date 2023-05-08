package com.ke.compose.music.ui.playlist

import com.ke.compose.music.MainApplication
import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import com.ke.music.api.response.Playlist
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetUserPlaylistUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Unit, List<Playlist>>(Dispatchers.IO) {
    override suspend fun execute(parameters: Unit): List<Playlist> {
        return httpService.getUserPlaylistList(MainApplication.currentUserId).playlist
    }
}