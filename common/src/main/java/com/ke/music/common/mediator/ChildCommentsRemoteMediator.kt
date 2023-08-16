package com.ke.music.common.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.ke.music.common.entity.CommentType
import com.ke.music.common.entity.IChildComment

@OptIn(ExperimentalPagingApi::class)
abstract class ChildCommentsRemoteMediator : RemoteMediator<Int, IChildComment>() {
    abstract var commentId: Long
    abstract var sourceId: Long
    abstract var commentType: CommentType
}