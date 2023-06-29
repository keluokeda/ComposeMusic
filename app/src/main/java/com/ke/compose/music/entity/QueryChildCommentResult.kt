package com.ke.compose.music.entity

data class QueryChildCommentResult(
    val parentCommentId: Long,
    val commentId: Long,
    val userId: Long,
    val username: String,
    val userAvatar: String,
    val userSignature: String?,
    val content: String?,
    val timeString: String,
    val ip: String,
    val likedCount: Int,
    /**
     * 点赞的时间
     */
    val likedTime: Long?,

    /**
     * 如果是回复别人的评论 就不是-1
     */
    val beRepliedCommentId: Long = -1,
    /**
     * 回复的人的用户名
     */
    val beRepliedUsername: String?
) {
    val liked: Boolean
        get() = likedTime != null
}
