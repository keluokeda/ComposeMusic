package com.ke.music.common.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.ke.music.common.entity.IMv

@OptIn(ExperimentalPagingApi::class)
abstract class AllMvRemoteMediator : RemoteMediator<Int, IMv>() {

    abstract var type: String
    abstract var area: String
}