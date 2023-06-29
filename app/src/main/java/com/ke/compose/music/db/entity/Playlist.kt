package com.ke.compose.music.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 歌单
 */
@Entity(tableName = "playlist")
data class Playlist(
    @PrimaryKey
    val id: Long,

    /**
     * 创建者id
     */
    @ColumnInfo("creator_id")
    val creatorId: Long,

    /**
     * 封面图片地址
     */
    @ColumnInfo(name = "cover_image_url")
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
    @ColumnInfo("track_count")
    val trackCount: Int,

    /**
     * 总播放次数
     */
    @ColumnInfo("play_count")
    val playCount: Int,

    /**
     * 更新时间
     */
    @ColumnInfo(name = "update_time")
    val updateTime: Long,


    /**
     * 分享次数
     */
    @ColumnInfo("share_count")
    val shareCount: Int,

    /**
     * 订阅数量
     */
    @ColumnInfo("booked_count")
    val bookedCount: Int,

    /**
     * 评论数量
     */
    @ColumnInfo("comment_count")
    val commentCount: Int
)
