package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.room.db.entity.Album
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LoadArtistAlbumsUseCase @Inject constructor(
    private val httpService: HttpService,
    private val albumRepository: com.ke.music.common.repository.AlbumRepository,
) :
    UseCase<Long, Unit>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long) {
        val response = httpService.getArtistAlbums(parameters)
        albumRepository.saveArtistAlbums(parameters, response.hotAlbums.map {
            Album(it.id, it.name, it.imageUrl)
        })
    }
}