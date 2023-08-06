package com.ke.music.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ke.music.api.HttpService
import com.ke.music.repository.ChildCommentRepository
import com.ke.music.room.entity.CommentType
import com.ke.music.room.entity.QueryChildCommentResult
import com.orhanobut.logger.Logger

@OptIn(ExperimentalPagingApi::class)
class ChildCommentsRemoteMediator constructor(
    private val httpService: HttpService,
    private val commentType: CommentType,
    private val sourceId: Long,
    private val commentId: Long,
    private val childCommentRepository: ChildCommentRepository
) : RemoteMediator<Int, QueryChildCommentResult>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private var cursor: Long = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, QueryChildCommentResult>
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






            childCommentRepository.saveChildCommentToRoom(
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
