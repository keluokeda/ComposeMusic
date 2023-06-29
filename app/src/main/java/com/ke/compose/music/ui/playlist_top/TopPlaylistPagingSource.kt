package com.ke.compose.music.ui.playlist_top

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ke.music.api.HttpService
import com.ke.music.api.response.Playlist

class TopPlaylistPagingSource constructor(
    private val httpService: HttpService,
    private val category: String?
) :
    PagingSource<Int, Playlist>() {


    override fun getRefreshKey(state: PagingState<Int, Playlist>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Playlist> {
        val index = params.key ?: 1
        val offset = (index - 1) * 50
        val response = httpService.getTopPlaylist(category, 50, offset)
        val preKey = if (index == 1) null else index - 1
        val nextKey = if (response.more) index + 1 else null
        return LoadResult.Page(response.playlists, preKey, nextKey)
    }
}