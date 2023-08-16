package com.ke.music.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import com.ke.music.api.HttpService
import com.ke.music.common.entity.IArtist
import com.ke.music.common.mediator.HotArtistRemoteMediator
import com.ke.music.common.repository.ArtistRepository
import javax.inject.Inject

class HotArtistListRemoteMediator @Inject constructor(
    private val httpService: HttpService,
    private val artistRepository: ArtistRepository,
) :
    HotArtistRemoteMediator() {

    private var offset = 0

    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, IArtist>,
    ): MediatorResult {
        try {

            when (loadType) {
                LoadType.REFRESH -> {
                    offset = 0
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    offset += 30
                }
            }

            val response = httpService.getArtistList(type, area, 30, offset)

            artistRepository.saveHotArtist(
                area, type, response.artists, offset == 0
            )

//            if (offset == 0) {
//                newAlbumDao.deleteAllByArea(area)
//                hotArtistDao.deleteAll()
//            }

//            hotArtistDao.insertAll(
//                response.artists.map {
//                    HotArtist(
//                        id = 0,
//                        artistId = it.id,
//                        name = it.name,
//                        avatar = it.avatar
//                    )
//                }
//            )

            return MediatorResult.Success(
                endOfPaginationReached = !response.more

            )

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}