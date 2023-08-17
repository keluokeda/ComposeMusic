package com.ke.music.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import com.ke.music.api.HttpService
import com.ke.music.common.entity.IArtist
import com.ke.music.common.mediator.HotArtistRemoteMediator
import com.ke.music.common.repository.ArtistRepository
import javax.inject.Inject

internal class HotArtistListRemoteMediator @Inject constructor(
    private val httpService: HttpService,
    private val artistRepository: ArtistRepository,
) :
    HotArtistRemoteMediator() {

    override var area: Int = -1

    override var type: Int = -1


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

            artistRepository.saveHotArtists(
                area, type, response.artists, offset == 0
            )
            return MediatorResult.Success(
                endOfPaginationReached = !response.more
            )

        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)
        }
    }
}