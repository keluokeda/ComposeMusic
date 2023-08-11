package com.ke.music.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ke.music.api.HttpService
import com.ke.music.room.db.dao.NewAlbumDao
import com.ke.music.room.db.entity.NewAlbum

@OptIn(ExperimentalPagingApi::class)
class NewAlbumListRemoteMediator constructor(
    private val httpService: HttpService,
    private val newAlbumDao: NewAlbumDao,
    var area: String
) :
    RemoteMediator<Int, NewAlbum>() {

    private var offset = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewAlbum>
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

            val response = httpService.getNewAlbumList(area, 30, offset)

            if (offset == 0) {
                newAlbumDao.deleteAllByArea(area)
            }

            newAlbumDao.insertAll(
                response.albums.map {
                    NewAlbum(
                        id = 0,
                        albumId = it.id,
                        area = area,
                        name = it.name,
                        subtitle = it.artist.name,
                        image = it.picUrl
                    )
                }
            )

            return MediatorResult.Success(
                endOfPaginationReached = response.albums.isEmpty()

            )

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}