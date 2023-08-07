package com.ke.music.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ke.music.api.HttpService
import com.ke.music.room.db.dao.HotArtistDao
import com.ke.music.room.db.entity.HotArtist

@OptIn(ExperimentalPagingApi::class)
class HotArtistListRemoteMediator constructor(
    private val httpService: HttpService,
    private val hotArtistDao: HotArtistDao,
    var area: Int = -1,
    var type: Int = -1
) :
    RemoteMediator<Int, HotArtist>() {

    private var offset = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HotArtist>
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

            if (offset == 0) {
//                newAlbumDao.deleteAllByArea(area)
                hotArtistDao.deleteAll()
            }

            hotArtistDao.insertAll(
                response.artists.map {
                    HotArtist(
                        id = 0,
                        artistId = it.id,
                        name = it.name,
                        avatar = it.avatar
                    )
                }
            )

            return MediatorResult.Success(
                endOfPaginationReached = !response.more

            )

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}