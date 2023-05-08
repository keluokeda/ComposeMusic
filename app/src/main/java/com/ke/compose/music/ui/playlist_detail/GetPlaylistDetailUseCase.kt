package com.ke.compose.music.ui.playlist_detail

import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPlaylistDetailUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Long, PlaylistDetailUiState.Detail>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long): PlaylistDetailUiState.Detail {

        return withContext(Dispatchers.IO) {
            val playlist = async {
                httpService.getPlaylistDetail(parameters).playlist
            }
            val songs = async {
                httpService.getPlaylistTracks(parameters).songs
            }

            val dynamic = async {
                httpService.getPlaylistDetailDynamic(parameters)
            }

            PlaylistDetailUiState.Detail.from(playlist.await(), songs.await(), dynamic.await())
        }
    }
}