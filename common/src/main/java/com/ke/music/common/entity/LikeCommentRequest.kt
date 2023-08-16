package com.ke.music.common.entity

data class LikeCommentRequest(
    val sourceId: Long,
    val commentType: CommentType,
    val commentId: Long,
    val isChildComment: Boolean = false,
)