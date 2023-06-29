package com.ke.compose.music.ui.playlist_highquality

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ke.music.api.HttpService
import com.ke.music.api.response.Playlist

class HighQualityPlaylistPagingSource constructor(
    private val tag: String,
    private val httpService: HttpService
) :
    PagingSource<Int, Playlist>() {
    override fun getRefreshKey(state: PagingState<Int, Playlist>): Int? = null

    private var before: Long? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Playlist> {
        val index = params.key ?: 1

        val response = httpService.getHighQualityPlaylists(tag, 50, before)
        before = response.playlists.lastOrNull()?.updateTime

        val preKey = if (index == 1) null else index - 1
        val nextKey = if (response.more) index + 1 else null
        return LoadResult.Page(
            data = response.playlists,
            nextKey = nextKey,
            prevKey = preKey
        )
    }
}