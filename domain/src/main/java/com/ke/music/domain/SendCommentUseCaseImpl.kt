package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.SendCommentUseCase
import com.ke.music.common.entity.SendCommentRequest
import com.ke.music.common.repository.CommentRepository
import javax.inject.Inject

class SendCommentUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val commentRepository: CommentRepository,
) : UseCase<SendCommentRequest, Boolean>(), SendCommentUseCase {

    override suspend fun execute(parameters: SendCommentRequest): Boolean {

        httpService.sendComment(
            if (parameters.commentId == null) 1 else 2,
            parameters.type.type,
            parameters.id,
            parameters.content,
            parameters.commentId
        )


        parameters.commentId?.apply {
            commentRepository.incrementChildCommentCount(this)
        }

        return true

    }
}