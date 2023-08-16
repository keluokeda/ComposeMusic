package com.ke.music.common.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.RemoteMediator
import com.ke.music.common.entity.CommentType
import com.ke.music.common.entity.IComment

@OptIn(ExperimentalPagingApi::class)
abstract class CommentsRemoteMediator : RemoteMediator<Int, IComment>() {
    abstract var commentType: CommentType

    abstract var sourceId: Long
}