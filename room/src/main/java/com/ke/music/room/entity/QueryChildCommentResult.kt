package com.ke.music.room.entity

import com.ke.music.common.entity.IChildComment
import com.ke.music.common.entity.IUser
import com.ke.music.room.db.entity.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class QueryChildCommentResult(
    val parentCommentId: Long,
    override val commentId: Long,
    val userId: Long,
    val username: String,
    val userAvatar: String,
    val userSignature: String?,
    override val content: String,
    override val timeString: String,
    override val ip: String,
    override val likedCount: Int,
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
    override val beRepliedUsername: String?,
) : IChildComment {
    override val liked: Boolean
        get() = likedTime != null

    override val key: Long
        get() = commentId

    override val replyCount: Int
        get() = 0

    override val user: IUser
        get() = User(userId, username, userAvatar, userSignature)
}
