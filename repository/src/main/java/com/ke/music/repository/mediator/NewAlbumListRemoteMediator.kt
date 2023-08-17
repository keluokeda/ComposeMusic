package com.ke.music.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import com.ke.music.api.HttpService
import com.ke.music.common.entity.IAlbum
import com.ke.music.common.mediator.NewAlbumsRemoteMediator
import com.ke.music.common.repository.AlbumRepository
import javax.inject.Inject

internal class NewAlbumListRemoteMediator @Inject constructor(
    private val httpService: HttpService,
    private val albumRepository: AlbumRepository,
) :
    NewAlbumsRemoteMediator() {

    override var area: String = "全部"


    private var offset = 0

    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, IAlbum>,
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

            albumRepository.saveNewAlbum(response.albums, area, offset == 0)
            return MediatorResult.Success(
                endOfPaginationReached = response.albums.isEmpty()
            )

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }
}