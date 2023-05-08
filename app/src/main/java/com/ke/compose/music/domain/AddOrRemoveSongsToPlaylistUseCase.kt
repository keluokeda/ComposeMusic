package com.ke.compose.music.domain

import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AddOrRemoveSongsToPlaylistUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<AddOrRemoveSongsToPlaylistRequest, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: AddOrRemoveSongsToPlaylistRequest): Boolean {
        val songIds = parameters.songIds.joinToString(",") { it.toString() }
        return httpService.addOrRemoveSongsToPlaylist(
            if (parameters.add) "add" else "del",
            parameters.playlistId,
            songIds
        ).success
    }
}

data class AddOrRemoveSongsToPlaylistRequest(
    val playlistId: Long,
    val songIds: List<Long>,
    val add: Boolean
)