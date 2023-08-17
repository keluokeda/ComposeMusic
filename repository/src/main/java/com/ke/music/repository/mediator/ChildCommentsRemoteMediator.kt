package com.ke.music.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import com.ke.music.api.HttpService
import com.ke.music.common.entity.CommentType
import com.ke.music.common.entity.IChildComment
import com.ke.music.common.mediator.ChildCommentsRemoteMediator
import com.ke.music.common.repository.CommentRepository
import com.orhanobut.logger.Logger

internal class ChildCommentsRemoteMediator constructor(
    private val httpService: HttpService,
    private val commentRepository: CommentRepository,
    override val commentId: Long,
    override val sourceId: Long,
    override val commentType: CommentType,
) : ChildCommentsRemoteMediator() {


    @ExperimentalPagingApi
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private var cursor: Long = 0

    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, IChildComment>,
    ): MediatorResult {


        Logger.d("开始加载子评论 $loadType $commentId")

        try {

            when (loadType) {
                LoadType.REFRESH -> {
                    cursor = 0
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                }
            }


            val response = httpService.getChildComments(
                id = sourceId,
                type = commentType.type,
                parentCommentId = commentId,
                limit = state.config.pageSize,
                time = cursor
            ).data






            commentRepository.saveChildComments(
                response.comments ?: emptyList(),
                commentId,
                cursor == 0L
            )


            cursor = response.time

//            childCommentDao.insertAll(response.comments?.map { it.convert(commentId) } ?: emptyList())

            return MediatorResult.Success(
                endOfPaginationReached = !response.hasMore
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return MediatorResult.Error(e)
        }


    }
}
