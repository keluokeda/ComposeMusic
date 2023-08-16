package com.ke.music.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import com.ke.music.api.HttpService
import com.ke.music.common.entity.CommentType
import com.ke.music.common.entity.IComment
import com.ke.music.common.mediator.CommentsRemoteMediator
import com.ke.music.common.repository.CommentRepository
import com.orhanobut.logger.Logger
import javax.inject.Inject

class CommentsRemoteMediator @Inject constructor(
    private val httpService: HttpService,
    private val commentRepository: CommentRepository,
) : CommentsRemoteMediator() {

    override var commentType: CommentType = CommentType.Music


    override var sourceId: Long = 0L

    /**
     * 排序方式, 1:按推荐排序, 2:按热度排序, 3:按时间排序
     */
    private val sortType: Int = 3

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }


    private var index = 1
    private var cursor: Long = 0


    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, IComment>,
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

            commentRepository.saveComments(
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

