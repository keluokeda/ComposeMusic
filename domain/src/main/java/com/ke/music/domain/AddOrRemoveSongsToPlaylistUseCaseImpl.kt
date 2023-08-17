package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.AddOrRemoveSongsToPlaylistUseCase
import com.ke.music.common.entity.AddOrRemoveSongsToPlaylistRequest
import javax.inject.Inject

internal class AddOrRemoveSongsToPlaylistUseCaseImpl
@Inject constructor(private val httpService: HttpService) :
    UseCase<AddOrRemoveSongsToPlaylistRequest, Boolean>(),
    AddOrRemoveSongsToPlaylistUseCase {

    override suspend fun execute(parameters: AddOrRemoveSongsToPlaylistRequest): Boolean {
        val songIds = parameters.songIds.joinToString(",") { it.toString() }
        httpService.addOrRemoveSongsToPlaylist(
            if (parameters.add) "add" else "del",
            parameters.playlistId,
            songIds
        )
        return true
    }
}