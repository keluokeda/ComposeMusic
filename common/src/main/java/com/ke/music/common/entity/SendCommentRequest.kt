package com.ke.music.common.entity

class SendCommentRequest(
    val type: CommentType,
    val id: Long,
    val commentId: Long?,
    val content: String,
)