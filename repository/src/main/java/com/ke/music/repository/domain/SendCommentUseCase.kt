package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.common.entity.SendCommentRequest
import com.ke.music.repository.CommentRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SendCommentUseCase @Inject constructor(
    private val httpService: HttpService,
    private val commentRepository: CommentRepository,
) :
    UseCase<SendCommentRequest, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: SendCommentRequest): Boolean {
        httpService.sendComment(
            if (parameters.commentId == null) 1 else 2,
            parameters.type.type,
            parameters.id,
            parameters.content,
            parameters.commentId
        )

        if (parameters.commentId != null) {
            val target = commentRepository.queryByCommentId(parameters.commentId!!)
            if (target != null) {
                commentRepository.update(
                    target.copy(
                        replyCount = target.replyCount + 1
                    )
                )
            }
        }


        return true
    }
}

