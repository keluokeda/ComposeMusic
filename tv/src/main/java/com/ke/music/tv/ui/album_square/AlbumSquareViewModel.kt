package com.ke.music.tv.ui.album_square

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.ke.music.common.entity.IAlbum
import com.ke.music.common.mediator.NewAlbumsRemoteMediator
import com.ke.music.common.repository.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class AlbumSquareViewModel @Inject constructor(
    private val albumRepository: AlbumRepository,
    newAlbumsRemoteMediator: NewAlbumsRemoteMediator,
) : ViewModel() {

    private val _area = MutableStateFlow("ALL")

    internal val area: StateFlow<String>
        get() = _area


    internal fun updateArea(value: String) {
        _area.value = value
    }

    init {
        newAlbumsRemoteMediator.area = area.value
    }


    @OptIn(ExperimentalPagingApi::class)
    val albumList = Pager(
        config = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            initialLoadSize = 50
        ),
        remoteMediator = newAlbumsRemoteMediator
    ) {
        albumRepository.getNewAlbums(area.value) as PagingSource<Int, IAlbum>
    }.flow
        .cachedIn(viewModelScope)

}
