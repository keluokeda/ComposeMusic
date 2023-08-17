package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.DeleteCommentUseCase
import com.ke.music.common.entity.DeleteCommentRequest
import com.ke.music.common.repository.CommentRepository
import javax.inject.Inject

internal class DeleteCommentUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val commentRepository: CommentRepository,
) :
    UseCase<DeleteCommentRequest, Boolean>(), DeleteCommentUseCase {

    override suspend fun execute(parameters: DeleteCommentRequest): Boolean {
        commentRepository.deleteComment(parameters.commentId, parameters.isChildComment)
        httpService.deleteComment(
            parameters.commentType.type,
            parameters.resourceId,
            parameters.commentId
        )
        return true
    }
}