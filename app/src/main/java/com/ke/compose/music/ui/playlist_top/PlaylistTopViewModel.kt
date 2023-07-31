package com.ke.compose.music.ui.playlist_top

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ke.compose.music.repository.PlaylistRepository
import com.ke.music.api.HttpService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PlaylistTopViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    httpService: HttpService,
    private val playlistRepository: PlaylistRepository
) :
    ViewModel() {
    internal val category = savedStateHandle.get<String>("category") ?: "全部"


    private val topPlaylistRemoteMediator = TopPlaylistRemoteMediator(
        httpService, category, playlistRepository
    )


    @OptIn(ExperimentalPagingApi::class)
    val playlists: Flow<PagingData<com.ke.compose.music.db.entity.Playlist>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            initialLoadSize = 50
        ),
        remoteMediator = topPlaylistRemoteMediator
    ) {
        playlistRepository.topPlaylist(category)
    }.flow
        .cachedIn(viewModelScope)


}