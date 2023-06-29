package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommentsResponse(
    val code: Int,
    val data: CommentData
)

@JsonClass(generateAdapter = true)
data class CommentData(


    /**
     * 子评论
     */
    val comments: List<Comment>? = null,

    val hasMore: Boolean,

    /**
     * 下一页会用到这个
     */
    val time: Long = -1L,

    /**
     * 同time，字符串类型的long
     */
    val cursor: String = "0",

    val totalCount: Int = 0
) {
    /**
     * 下一条查询会用到的参数
     */
    val nextPageCursor: Long
        get() = if (time == -1L) cursor.toLong() else time
}
