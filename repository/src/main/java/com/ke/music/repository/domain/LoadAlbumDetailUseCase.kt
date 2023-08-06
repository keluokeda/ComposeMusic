package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.repository.AlbumRepository
import kotlinx.coroutines.Dispatchers
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