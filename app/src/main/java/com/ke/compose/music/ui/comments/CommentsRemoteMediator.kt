package com.ke.compose.music.ui.comments

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ke.compose.music.db.Comment
import com.ke.compose.music.db.CommentDao
import com.ke.music.api.HttpService
import com.orhanobut.logger.Logger

@OptIn(ExperimentalPagingApi::class)
class CommentsRemoteMediator(
    private val commentDao: CommentDao,
    private val httpService: HttpService,
    private val sourceId: Long,
    private val commentType: CommentType,
    /**
     * 排序方式, 1:按推荐排序, 2:按热度排序, 3:按时间排序
     */
    internal var sortType: Int
) : RemoteMediator<Int, Comment>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    private var index = 1
    private var cursor: Long = 0


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Comment>
    ): MediatorResult {

        Logger.d("开始加载数据 $loadType $index")
        when (loadType) {
            LoadType.REFRESH -> {
                commentDao.deleteAll()
                index = 1
            }

            LoadType.PREPEND -> {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            LoadType.APPEND -> {
                index += 1
            }
        }


        val response = httpService.getComments(
            id = sourceId,
            type = commentType.type,
            sortType = sortType,
            pageSize = state.config.pageSize,
            pageNo = index,
            cursor = cursor
        ).data

        if (sortType == 3)
            cursor = response.nextPageCursor


        commentDao.insertAll(response.comments?.map { convert(it) } ?: emptyList())



        return MediatorResult.Success(
            endOfPaginationReached = !response.hasMore
        )
    }
}

private fun convert(comment: com.ke.music.api.response.Comment): Comment {

    return Comment(
        0,
        comment.commentId,
        comment.user.nickname,
        comment.user.avatarUrl,
        comment.user.userId,
        comment.content ?: "",
        comment.timeString,
        comment.time,
        comment.likedCount,
        comment.ipLocation.location,
        comment.owner,
        comment.liked,
        comment.replyCount
    )
}