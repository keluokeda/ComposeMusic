package com.ke.compose.music.ui.comments

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ke.compose.music.entity.QueryCommentResult
import com.ke.compose.music.repository.CommentRepository
import com.ke.music.api.HttpService
import com.orhanobut.logger.Logger

@OptIn(ExperimentalPagingApi::class)
class CommentsRemoteMediator(
    private val httpService: HttpService,
    private val sourceId: Long,
    private val commentType: CommentType,

    private val commentRepository: CommentRepository
) : RemoteMediator<Int, QueryCommentResult>() {

    /**
     * 排序方式, 1:按推荐排序, 2:按热度排序, 3:按时间排序
     */
    private val sortType: Int = 3

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    private var index = 1
    private var cursor: Long = 0


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, QueryCommentResult>
    ): MediatorResult {

        Logger.d("开始加载数据 $loadType $index")

        try {
            when (loadType) {
                LoadType.REFRESH -> {
                    index = 1
                    cursor = 0L
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

            commentRepository.insertIntoRoom(
                response.comments ?: emptyList(),
                commentType,
                sourceId,
                deleteOld = index == 1
            )


            return MediatorResult.Success(
                endOfPaginationReached = !response.hasMore
            )
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }


    }
}

