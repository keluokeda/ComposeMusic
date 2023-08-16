package com.ke.music.common.entity

data class DeleteCommentRequest(
    val commentType: CommentType,
    val resourceId: Long,
    val commentId: Long,
    val isChildComment: Boolean,
)