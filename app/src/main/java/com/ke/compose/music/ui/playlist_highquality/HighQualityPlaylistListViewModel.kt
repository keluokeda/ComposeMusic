package com.ke.compose.music.ui.playlist_highquality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ke.music.api.HttpService
import com.ke.music.api.response.Playlist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HighQualityPlaylistListViewModel @Inject constructor(private val httpService: HttpService) :
    ViewModel() {
    internal var tag: String = ""
        set(value) {
            field = value
            highQualityPlaylistPagingSource?.invalidate()
        }

    private var highQualityPlaylistPagingSource: HighQualityPlaylistPagingSource? = null

    val pagingData: Flow<PagingData<Playlist>> = Pager(
        config = PagingConfig(
            50,
            enablePlaceholders = false,
            initialLoadSize = 50,
            prefetchDistance = 1
        )
    ) {
        HighQualityPlaylistPagingSource(tag, httpService).apply {
            highQualityPlaylistPagingSource = this
        }
    }.flow.cachedIn(viewModelScope)
}