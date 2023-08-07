package com.ke.compose.music.ui.album_square

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.ke.music.api.HttpService
import com.ke.music.repository.mediator.NewAlbumListRemoteMediator
import com.ke.music.room.db.dao.NewAlbumDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

internal abstract class AlbumSquareViewModel constructor(
    private val newAlbumDao: NewAlbumDao,
    httpService: HttpService,
    private val area: String
) : ViewModel() {


    private val remoteMediator = NewAlbumListRemoteMediator(httpService, newAlbumDao, area)

    @OptIn(ExperimentalPagingApi::class)
    internal val albumList = Pager(
        config = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            initialLoadSize = 50
        ),
        remoteMediator = remoteMediator
    ) {
//        commentRepository.getComments(id, commentType)
        newAlbumDao.getAlbumListByArea(area)
    }.flow
        .cachedIn(viewModelScope)

}

@HiltViewModel
internal class AllAlbumSquareViewModel @Inject constructor(
    private val httpService: HttpService,
    private val newAlbumDao: NewAlbumDao
) : AlbumSquareViewModel(newAlbumDao, httpService, "ALL")

@HiltViewModel
internal class ZhAlbumSquareViewModel @Inject constructor(
    private val httpService: HttpService,
    private val newAlbumDao: NewAlbumDao
) : AlbumSquareViewModel(newAlbumDao, httpService, "ZH")

@HiltViewModel
internal class EaAlbumSquareViewModel @Inject constructor(
    private val httpService: HttpService,
    private val newAlbumDao: NewAlbumDao
) : AlbumSquareViewModel(newAlbumDao, httpService, "EA")

@HiltViewModel
internal class KrAlbumSquareViewModel @Inject constructor(
    private val httpService: HttpService,
    private val newAlbumDao: NewAlbumDao
) : AlbumSquareViewModel(newAlbumDao, httpService, "KR")

@HiltViewModel
internal class JpAlbumSquareViewModel @Inject constructor(
    private val httpService: HttpService,
    private val newAlbumDao: NewAlbumDao
) : AlbumSquareViewModel(newAlbumDao, httpService, "JP")