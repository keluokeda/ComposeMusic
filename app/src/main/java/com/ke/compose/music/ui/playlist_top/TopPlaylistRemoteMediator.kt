package com.ke.compose.music.ui.playlist_top

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ke.compose.music.db.entity.Playlist
import com.ke.compose.music.repository.PlaylistRepository
import com.ke.music.api.HttpService

@OptIn(ExperimentalPagingApi::class)
class TopPlaylistRemoteMediator constructor(
    private val httpService: HttpService,
    internal var category: String?,
    private val playlistRepository: PlaylistRepository
) : RemoteMediator<Int, Playlist>() {


    private var offset = 0


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Playlist>
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

            if (offset == 0) {
                playlistRepository.deleteTopPlaylistsByCategory(category = category)
            }

            playlistRepository.saveTopPlaylists(response)

            return MediatorResult.Success(
                endOfPaginationReached =
//                true
                !response.more
            )
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }


}