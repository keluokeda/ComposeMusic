package com.ke.music.common.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.ke.music.common.entity.IPlaylist

@OptIn(ExperimentalPagingApi::class)
abstract class TopPlaylistRemoteMediator : RemoteMediator<Int, IPlaylist>() {
    abstract val category: String?
}