package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaylistDynamicResponse(
    /**
     * 分享次数
     */
    val shareCount: Int,
    /**
     * 播放次数
     */
    val playCount: Int,
    /**
     * 订阅数量
     */
    val bookedCount: Int,
    /**
     * 是否订阅了
     */
    val subscribed: Boolean,

    /**
     * 评论数量
     */
    val commentCount: Int
)
