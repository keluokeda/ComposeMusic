package com.ke.compose.music.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 专辑详情
 */
@Entity(tableName = "album_detail")
data class AlbumDetail(
    @PrimaryKey
    @ColumnInfo("album_id")
    val albumId: Long,
    /**
     * 发布时间
     */
    @ColumnInfo("publish_time")
    val publishTime: Long,
    val company: String,

    val description: String?,

    @ColumnInfo("artist_id")
    val artistId: Long,


    )
