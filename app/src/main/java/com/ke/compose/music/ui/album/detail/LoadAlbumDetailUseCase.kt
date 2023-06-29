package com.ke.compose.music.ui.album.detail

import android.annotation.SuppressLint
import com.ke.compose.music.domain.UseCase
import com.ke.compose.music.repository.AlbumRepository
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import javax.inject.Inject

class LoadAlbumDetailUseCase @Inject constructor(
    private val httpService: HttpService,
    private val albumRepository: AlbumRepository
) :
    UseCase<Long, Unit>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long) {
        val response = httpService.getAlbumDetail(parameters)

        val isSub = httpService.getAlbumDynamic(parameters).isSub

        albumRepository.saveAlbumResponseToRoom(response, isSub)

//        return AlbumDetailUiState.Detail(
//            id = response.album.id,
//            name = response.album.name,
//            description = response.album.description,
//            artistName = response.album.artist.name,
//            artistId = response.album.id,
//            image = response.album.picUrl,
//            publishTime = simpleDateFormat.format(Date(response.album.publishTime)),
//            songs = response.songs
//        )
    }
}

@SuppressLint("SimpleDateFormat")
private val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd")