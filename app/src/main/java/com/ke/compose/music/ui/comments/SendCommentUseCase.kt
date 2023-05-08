package com.ke.compose.music.ui.comments

import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class SendCommentUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<SendCommentRequest, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: SendCommentRequest): Boolean {
        httpService.sendComment(
            if (parameters.commentId == null) 1 else 2,
            parameters.type.type,
            parameters.id,
            parameters.content,
            parameters.commentId
        )
        return true
    }
}

class SendCommentRequest(
    val type: CommentType,
    val id: Long,
    val commentId: Long?,
    val content: String
)