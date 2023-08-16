package com.ke.music.common.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.ke.music.common.entity.IAlbum

@OptIn(ExperimentalPagingApi::class)
abstract class NewAlbumsRemoteMediator : RemoteMediator<Int, IAlbum>() {
    abstract var area: String
}