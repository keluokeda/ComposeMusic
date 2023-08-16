package com.ke.music.room.entity

import com.ke.music.common.entity.IComment
import com.ke.music.common.entity.IUser
import com.ke.music.room.db.entity.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class QueryCommentAndUserResult(
    override val commentId: Long,
    override val content: String,
    val username: String,
    val userAvatar: String,
    val userId: Long,
    val time: String,
) : IComment {
    override val user: IUser
        get() = User(userId, username, userAvatar, null)
    override val timeString: String
        get() = ""
    override val ip: String
        get() = ""
    override val likedCount: Int
        get() = 0
    override val replyCount: Int
        get() = 0
    override val liked: Boolean
        get() = false
    override val key: Long
        get() = commentId
}
