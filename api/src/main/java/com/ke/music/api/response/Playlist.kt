package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Playlist(
    val id: Long,
    /**
     * 创建者
     */
    val creator: User,
    /**
     * 封面图片地址
     */
    val coverImgUrl: String,
    /**
     * 名字
     */
    val name: String,
    /**
     * 标签
     */
    val tags: List<String>,
    /**
     * 描述
     */
    val description: String?,
    /**
     * 歌曲数量
     */
    val trackCount: Int,
    /**
     * 总播放次数
     */
    val playCount: Int,

    /**
     * 订阅用户列表
     */
    val subscribers: List<User> = emptyList(),
)
