package com.ke.music.tv.ui.playlist_top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ke.music.api.HttpService
import com.ke.music.api.response.PlaylistCategory
import com.ke.music.repository.PlaylistRepository
import com.ke.music.repository.domain.GetPlaylistCategoryListUseCase
import com.ke.music.repository.domain.successOr
import com.ke.music.repository.mediator.TopPlaylistRemoteMediator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistTopViewModel @Inject constructor(
    httpService: HttpService,
    private val playlistRepository: PlaylistRepository,
    private val getPlaylistCategoryListUseCase: GetPlaylistCategoryListUseCase
) :
    ViewModel() {

    internal var selectedCategory: String = "全部"
        set(value) {
            topPlaylistRemoteMediator.category = value
            field = value
        }

    private val topPlaylistRemoteMediator = TopPlaylistRemoteMediator(
        httpService, selectedCategory, playlistRepository
    )

    private val _categoryList = MutableStateFlow(emptyList<PlaylistCategory>())
    internal val categoryList: StateFlow<List<PlaylistCategory>>
        get() = _categoryList


    @OptIn(ExperimentalPagingApi::class)
    val playlists: Flow<PagingData<com.ke.music.room.db.entity.Playlist>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false,
            initialLoadSize = 30
        ),
        remoteMediator = topPlaylistRemoteMediator
    ) {
        playlistRepository.topPlaylist(selectedCategory)
    }.flow
        .cachedIn(viewModelScope)


    init {

        viewModelScope.launch {
            _categoryList.value = getPlaylistCategoryListUseCase(Unit).successOr(emptyList())
        }
    }
}

//@HiltViewModel
//internal class PlaylistTopListViewModel @Inject constructor(
//    httpService: HttpService,
//    private val playlistRepository: PlaylistRepository,
//) : ViewModel() {
//
//}