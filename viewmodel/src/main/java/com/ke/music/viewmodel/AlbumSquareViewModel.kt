package com.ke.music.viewmodel

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
import javax.inject.Inject

abstract class AlbumSquareViewModel constructor(
    private val albumRepository: AlbumRepository,
    newAlbumsRemoteMediator: NewAlbumsRemoteMediator,
    private val area: String,
) : ViewModel() {

    init {
        newAlbumsRemoteMediator.area = area
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
        albumRepository.getNewAlbums(area) as PagingSource<Int, IAlbum>
    }.flow
        .cachedIn(viewModelScope)

}

@HiltViewModel
class AllAlbumSquareViewModel @Inject constructor(
    albumRepository: AlbumRepository,
    newAlbumsRemoteMediator: NewAlbumsRemoteMediator,
) : AlbumSquareViewModel(albumRepository, newAlbumsRemoteMediator, "ALL")

@HiltViewModel
class ZhAlbumSquareViewModel @Inject constructor(
    albumRepository: AlbumRepository,
    newAlbumsRemoteMediator: NewAlbumsRemoteMediator,
) : AlbumSquareViewModel(albumRepository, newAlbumsRemoteMediator, "ZH")

@HiltViewModel
class EaAlbumSquareViewModel @Inject constructor(
    albumRepository: AlbumRepository,
    newAlbumsRemoteMediator: NewAlbumsRemoteMediator,
) : AlbumSquareViewModel(albumRepository, newAlbumsRemoteMediator, "EA")

@HiltViewModel
class KrAlbumSquareViewModel @Inject constructor(
    albumRepository: AlbumRepository,
    newAlbumsRemoteMediator: NewAlbumsRemoteMediator,
) : AlbumSquareViewModel(albumRepository, newAlbumsRemoteMediator, "KR")

@HiltViewModel
class JpAlbumSquareViewModel @Inject constructor(
    albumRepository: AlbumRepository,
    newAlbumsRemoteMediator: NewAlbumsRemoteMediator,
) : AlbumSquareViewModel(albumRepository, newAlbumsRemoteMediator, "JP")