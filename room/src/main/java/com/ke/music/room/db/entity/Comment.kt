package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ke.music.room.entity.CommentType

//
//@Entity(tableName = "comment")
//data class Comment(
//    @PrimaryKey(autoGenerate = true)
//    val id: Long = 0,
//    /**
//     * 评论id
//     */
//    val commentId: Long,
//    val username: String,
//    @ColumnInfo(name = "user_avatar")
//    val userAvatar: String,
//    @ColumnInfo(name = "user_id")
//    val userId: Long,
//    val content: String,
//    @ColumnInfo("time_string")
//    val timeString: String,
//    val time: Long,
//    @ColumnInfo("like_count")
//    val likedCount: Int,
//
//    @ColumnInfo("ip_location")
//    val ipLocation: String,
//    val owner: Boolean,
//    val liked: Boolean,
//
//
//    /**
//     * 子评论数
//     */
//    val replyCount: Int = 0,
//
//    )

@Entity(tableName = "comment")
data class Comment(
    @PrimaryKey
    val id: Long,

    /**
     * 用户id
     */
    @ColumnInfo("user_id")
    val userId: Long,


    /**
     * 评论内容
     */
    val content: String,

    val ip: String,

    @ColumnInfo("time_string")
    val timeString: String,

    val time: Long,


    @ColumnInfo("like_count")
    val likedCount: Int,

    /**
     * 子评论数
     */
    @ColumnInfo("reply_count")
    val replyCount: Int,

    /**
     * 评论类型
     */
    val type: CommentType,

    /**
     * 来源id
     */
    @ColumnInfo(name = "source_id")
    val sourceId: Long
)