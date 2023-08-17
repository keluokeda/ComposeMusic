package com.ke.music.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.ke.music.common.entity.IPlaylist
import com.ke.music.common.mediator.TopPlaylistRemoteMediator
import com.ke.music.common.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PlaylistTopViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val playlistRepository: PlaylistRepository,
    topPlaylistRemoteMediator: TopPlaylistRemoteMediator,
) :
    ViewModel() {
    val category = savedStateHandle.get<String>("category") ?: "全部"


    @OptIn(ExperimentalPagingApi::class)
    val playlists: Flow<PagingData<IPlaylist>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            initialLoadSize = 50
        ),
        remoteMediator = topPlaylistRemoteMediator
    ) {
        playlistRepository.topPlaylist(category) as PagingSource<Int, IPlaylist>
    }.flow
        .cachedIn(viewModelScope)


}