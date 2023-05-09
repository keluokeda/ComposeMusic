package com.ke.compose.music.ui.playlist_detail

import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DeleteSongFromPlaylistUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Pair<Long, Long>, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: Pair<Long, Long>): Boolean {
        return httpService.addOrRemoveSongsToPlaylist(
            "del",
            parameters.first,
            parameters.second.toString()
        ).success
    }
}