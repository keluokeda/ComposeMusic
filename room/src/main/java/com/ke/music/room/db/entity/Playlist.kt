package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ke.music.common.entity.IPlaylist
import kotlinx.parcelize.Parcelize

/**
 * 歌单
 */
@Parcelize
@Entity(tableName = "playlist")
data class Playlist(
    @PrimaryKey
    override val id: Long,

    /**
     * 创建者id
     */
    @ColumnInfo("creator_id")
    override val creatorId: Long,

    /**
     * 封面图片地址
     */
    @ColumnInfo(name = "cover_image_url")
    override val coverImgUrl: String,
    /**
     * 名字
     */
    override val name: String,
    /**
     * 标签
     */
    override val tags: List<String>,
    /**
     * 描述
     */
    override val description: String?,
    /**
     * 歌曲数量
     */
    @ColumnInfo("track_count")
    override val trackCount: Int,

    /**
     * 总播放次数
     */
    @ColumnInfo("play_count")
    override val playCount: Int,

    /**
     * 更新时间
     */
    @ColumnInfo(name = "update_time")
    override val updateTime: Long,


    /**
     * 分享次数
     */
    @ColumnInfo("share_count")
    override val shareCount: Int,

    /**
     * 订阅数量
     */
    @ColumnInfo("booked_count")
    override val bookedCount: Int,

    /**
     * 评论数量
     */
    @ColumnInfo("comment_count")
    override val commentCount: Int,
) : IPlaylist {

    val hasPayload: Boolean
        get() = shareCount != 0 || bookedCount != 0 || commentCount != 0
}
