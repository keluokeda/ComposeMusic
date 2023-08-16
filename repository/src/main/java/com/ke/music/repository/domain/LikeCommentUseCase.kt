package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.common.entity.LikeCommentRequest
import com.ke.music.repository.ChildCommentRepository
import com.ke.music.repository.CommentRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LikeCommentUseCase @Inject constructor(
    private val httpService: HttpService,
    private val commentRepository: CommentRepository,
    private val childCommentRepository: ChildCommentRepository,
) :
    UseCase<LikeCommentRequest, Boolean>(Dispatchers.IO) {
    override suspend fun execute(parameters: LikeCommentRequest): Boolean {

        val result = if (parameters.isChildComment) {
            childCommentRepository.toggleLikeComment(parameters.commentId)
        } else {
            commentRepository.toggleLikeComment(parameters.commentId)
        }
        return httpService.likeComment(
            parameters.sourceId,
            parameters.commentId,
            parameters.commentType.type,
            if (result) 1 else 0
        ).success
    }
}

