package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.room.db.dao.ArtistDescriptionDao
import com.ke.music.room.db.entity.ArtistDescription
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LoadArtistDescriptionUseCase
@Inject constructor(
    private val httpService: HttpService,
    private val artistDescriptionDao: ArtistDescriptionDao
) : UseCase<Long, Unit>(Dispatchers.IO) {
    override suspend fun execute(parameters: Long) {
        val response = httpService.getArtistDesc(parameters)

        val list = mutableListOf<ArtistDescription>()
        list.add(
            ArtistDescription(
                artistId = parameters,
                title = "简介",
                content = response.briefDesc
            )
        )

        response.introduction.forEach {
            list.add(
                ArtistDescription(
                    artistId = parameters,
                    title = it.ti,
                    content = it.txt
                )
            )
        }

        artistDescriptionDao.resetArtistDescription(parameters, list)

    }
}