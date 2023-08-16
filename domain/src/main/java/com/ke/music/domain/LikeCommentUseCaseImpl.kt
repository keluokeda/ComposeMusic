package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.LikeCommentUseCase
import com.ke.music.common.entity.LikeCommentRequest
import com.ke.music.common.repository.CommentRepository
import javax.inject.Inject

class LikeCommentUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val commentRepository: CommentRepository,
) :
    UseCase<LikeCommentRequest, Boolean>(), LikeCommentUseCase {

    override suspend fun execute(parameters: LikeCommentRequest): Boolean {
        val result = commentRepository.toggleLiked(parameters.commentId, parameters.isChildComment)
        return httpService.likeComment(
            parameters.sourceId,
            parameters.commentId,
            parameters.commentType.type,
            if (result) 1 else 0
        ).success
    }
}