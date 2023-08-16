package com.ke.music.common.entity

import android.os.Parcelable

interface IPlaylist : Parcelable {
    val id: Long

    /**
     * 创建者id
     */
    val creatorId: Long

    /**
     * 封面图片地址
     */
    val coverImgUrl: String

    /**
     * 名字
     */
    val name: String

    /**
     * 标签
     */
    val tags: List<String>

    /**
     * 描述
     */
    val description: String?

    /**
     * 歌曲数量
     */
    val trackCount: Int

    /**
     * 总播放次数
     */
    val playCount: Int

    /**
     * 更新时间
     */
    val updateTime: Long


    /**
     * 分享次数
     */
    val shareCount: Int

    /**
     * 订阅数量
     */
    val bookedCount: Int

    /**
     * 评论数量
     */
    val commentCount: Int
}