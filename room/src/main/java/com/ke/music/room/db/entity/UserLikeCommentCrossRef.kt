package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "user_like_comment_cross_ref",
    primaryKeys = ["user_id", "comment_id", "is_child_comment"]
)
data class UserLikeCommentCrossRef(
    @ColumnInfo("user_id")
    val userId: Long,
    @ColumnInfo("comment_id")
    val commentId: Long,

    /**
     * 是不是子评论
     */
    @ColumnInfo("is_child_comment")
    val isChildComment: Boolean,

    /**
     * 创建时间
     */
    @ColumnInfo("created_time")
    val createdTime: Long = System.currentTimeMillis()
)
