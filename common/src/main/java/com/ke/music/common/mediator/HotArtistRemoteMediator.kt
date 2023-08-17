package com.ke.music.common.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.ke.music.common.entity.IArtist

@OptIn(ExperimentalPagingApi::class)
abstract class HotArtistRemoteMediator : RemoteMediator<Int, IArtist>() {
    abstract var type: Int
    abstract var area: Int
}