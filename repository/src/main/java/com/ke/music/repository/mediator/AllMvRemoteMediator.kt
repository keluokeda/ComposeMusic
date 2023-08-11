package com.ke.music.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ke.music.api.HttpService
import com.ke.music.repository.MvRepository
import com.ke.music.room.db.entity.Mv
import com.orhanobut.logger.BuildConfig

@OptIn(ExperimentalPagingApi::class)
class AllMvRemoteMediator constructor(
    private val httpService: HttpService,
    private val mvRepository: MvRepository,
    var area: String,
    var type: String
) :
    RemoteMediator<Int, Mv>() {

    private var offset = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Mv>
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

            val response = httpService.getAllMv(type, area, 30, offset)

            val deleteOld = offset == 0

            mvRepository.saveAllMv(area, type, response.data, deleteOld)

//            if (offset == 0) {
//                hotArtistDao.deleteAll()
//            }
//
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
                endOfPaginationReached = !response.hasMore
            )

        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            return MediatorResult.Error(e)
        }
    }
}