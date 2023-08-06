package com.ke.music.room.entity

data class QueryCommentResult(
    val commentId: Long,
    val userId: Long,
    val username: String,
    val userAvatar: String,
    val userSignature: String?,
    val content: String,
    val timeString: String,
    val ip: String,
    val likedCount: Int,
    val replyCount: Int,
    /**
     * 点赞的时间
     */
    val likedTime: Long?
) {
    val liked: Boolean
        get() = likedTime != null
}
