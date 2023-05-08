package com.ke.compose.music.ui.comments

import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

internal class LikeCommentUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<LikeCommentRequest, Boolean>(Dispatchers.IO) {
    override suspend fun execute(parameters: LikeCommentRequest): Boolean {
        return httpService.likeComment(
            parameters.sourceId,
            parameters.commentId,
            parameters.commentType.type,
            if (parameters.like) 1 else 0
        ).success
    }
}

internal data class LikeCommentRequest(
    val sourceId: Long,
    val commentType: CommentType,
    val commentId: Long,
    val like: Boolean
)