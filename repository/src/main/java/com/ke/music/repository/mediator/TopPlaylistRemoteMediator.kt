package com.ke.music.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import com.ke.music.api.HttpService
import com.ke.music.common.entity.IPlaylist
import com.ke.music.common.mediator.TopPlaylistRemoteMediator
import com.ke.music.common.repository.PlaylistRepository

class TopPlaylistRemoteMediator constructor(
    private val httpService: HttpService,
    private val playlistRepository: PlaylistRepository,
    override val category: String?,
) : TopPlaylistRemoteMediator() {


    private var offset = 0


    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, IPlaylist>,
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


            val response = httpService.getTopPlaylist(
                category = category,
                limit = 30,
                offset = offset
            )



            playlistRepository.saveTopPlaylists(response, offset == 0)

            return MediatorResult.Success(
                endOfPaginationReached =
                !response.more
            )
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }


}