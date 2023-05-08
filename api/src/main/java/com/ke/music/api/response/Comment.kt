package com.ke.music.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Comment(
    val commentId: Long = 0,
    val user: User,
    val content: String?,
    @Json(name = "timeStr")
    val timeString: String = "",
    val time: Long = 0,
    val likedCount: Int = 0,
    val ipLocation: IpLocation,
    val owner: Boolean = false,
    val liked: Boolean = false,

    val beReplied: List<Comment>?,

    /**
     * 如果是回复别人的评论 就不是-1
     */
    val beRepliedCommentId: Long = -1,


    /**
     * 子评论数
     */
    val replyCount: Int = 0
)

@JsonClass(generateAdapter = true)
data class IpLocation(
    val location: String = ""
)