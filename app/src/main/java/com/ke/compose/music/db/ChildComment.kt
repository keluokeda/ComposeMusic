package com.ke.compose.music.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "child_comment")
data class ChildComment(
    /**
     * 主键
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo("comment_id")
    val commentId: Long,
    val username: String,
    @ColumnInfo(name = "user_avatar")
    val userAvatar: String,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    val content: String,
    @ColumnInfo("time_string")
    val timeString: String,
    val time: Long,
    @ColumnInfo("like_count")
    val likedCount: Int,

    @ColumnInfo("ip_location")
    val ipLocation: String,
    val owner: Boolean,
    val liked: Boolean,

    /**
     * 如果是回复别人的评论 就不是-1
     */
    @ColumnInfo("be_replied_comment_id")
    val beRepliedCommentId: Long = -1,
    /**
     * 回复的人的用户名
     */
    @ColumnInfo("be_replied_username")
    val beRepliedUsername: String?
)
