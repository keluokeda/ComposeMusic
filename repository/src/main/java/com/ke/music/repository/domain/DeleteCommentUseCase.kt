package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.common.entity.DeleteCommentRequest
import com.ke.music.repository.ChildCommentRepository
import com.ke.music.repository.CommentRepository
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

