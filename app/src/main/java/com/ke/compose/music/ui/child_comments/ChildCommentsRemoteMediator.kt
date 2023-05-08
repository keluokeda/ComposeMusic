package com.ke.compose.music.ui.child_comments

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.ke.compose.music.db.ChildComment
import com.ke.compose.music.db.ChildCommentDao
import com.ke.compose.music.ui.comments.CommentType
import com.ke.music.api.HttpService
import com.ke.music.api.response.Comment
import com.orhanobut.logger.Logger

@OptIn(ExperimentalPagingApi::class)
class ChildCommentsRemoteMediator constructor(
    private val httpService: HttpService,
    private val commentType: CommentType,
    private val sourceId: Long,
    private val commentId: Long,
    private val childCommentDao: ChildCommentDao,
    private val rootCommentFindEvent: (Comment) -> Unit
) : RemoteMediator<Int, ChildComment>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    private var cursor: Long = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ChildComment>
    ): MediatorResult {

        Logger.d("开始加载子评论 $loadType $commentId")


        when (loadType) {
            LoadType.REFRESH -> {
                childCommentDao.deleteAll()
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





        cursor = response.time

        if (response.ownerComment != null) {
            rootCommentFindEvent(response.ownerComment!!)
        }


        childCommentDao.insertAll(response.comments?.map { it.convert(commentId) } ?: emptyList())

        return MediatorResult.Success(
            endOfPaginationReached = !response.hasMore
        )
    }
}

private fun Comment.convert(parentCommentId: Long): ChildComment {
    val beRepliedUsername: String? =
        if (beReplied?.firstOrNull()?.beRepliedCommentId == parentCommentId) null else beReplied?.firstOrNull()?.user?.nickname

    return ChildComment(
        id = 0,
        commentId = commentId,
        username = user.nickname,
        userAvatar = user.avatarUrl,
        userId = user.userId,
        content = content?:"",
        timeString = timeString,
        time = time,
        likedCount = likedCount,
        ipLocation = ipLocation.location,
        owner = owner,
        liked = liked,
        beRepliedCommentId = beRepliedCommentId,
        beRepliedUsername = beRepliedUsername
    )
}