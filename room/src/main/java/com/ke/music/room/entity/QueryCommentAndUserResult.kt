package com.ke.music.room.entity

data class QueryCommentAndUserResult(
    val commentId: Long,
    val content: String,
    val username: String,
    val userAvatar: String,
    val userId: Long,
    val time: String
)
