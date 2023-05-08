package com.ke.compose.music.ui.album.detail

import android.annotation.SuppressLint
import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class GetAlbumDetailUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Long, AlbumDetailUiState.Detail>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long): AlbumDetailUiState.Detail {
        val response = httpService.getAlbumDetail(parameters)

        return AlbumDetailUiState.Detail(
            id = response.album.id,
            name = response.album.name,
            description = response.album.description,
            artistName = response.album.artist.name,
            artistId = response.album.id,
            image = response.album.picUrl,
            publishTime = simpleDateFormat.format(Date(response.album.publishTime)),
            songs = response.songs
        )
    }
}

@SuppressLint("SimpleDateFormat")
private val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")