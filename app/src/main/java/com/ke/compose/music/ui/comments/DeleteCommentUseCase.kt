package com.ke.compose.music.ui.comments

import com.ke.compose.music.domain.UseCase
import com.ke.compose.music.repository.ChildCommentRepository
import com.ke.compose.music.repository.CommentRepository
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DeleteCommentUseCase @Inject constructor(
    private val httpService: HttpService,
    private val commentRepository: CommentRepository,
    private val childCommentRepository: ChildCommentRepository
) :
    UseCase<DeleteCommentRequest, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: DeleteCommentRequest): Boolean {

        if (parameters.isChildComment) {
            childCommentRepository.deleteComment(parameters.commentId)
        } else {
            commentRepository.deleteById(parameters.commentId)
        }

        httpService.deleteComment(
            parameters.commentType.type,
            parameters.resourceId,
            parameters.commentId
        )
        return true
    }
}

data class DeleteCommentRequest(
    val commentType: CommentType,
    val resourceId: Long,
    val commentId: Long,
    val isChildComment: Boolean
)