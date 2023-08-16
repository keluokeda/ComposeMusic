package com.ke.music.room.entity

import com.ke.music.common.entity.IComment
import com.ke.music.common.entity.IUser
import com.ke.music.room.db.entity.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class QueryCommentResult(
    override val commentId: Long,
    val userId: Long,
    val username: String,
    val userAvatar: String,
    val userSignature: String?,
    override val content: String,
    override val timeString: String,
    override val ip: String,
    override val likedCount: Int,
    override val replyCount: Int,
    /**
     * 点赞的时间
     */
    val likedTime: Long?,
) : IComment {
    override val liked: Boolean
        get() = likedTime != null

    override val key: Long
        get() = commentId

    override val user: IUser
        get() = User(userId, username, userAvatar, userSignature)
}
