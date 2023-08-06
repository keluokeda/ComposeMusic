package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * 音乐和歌手中间表
 */
@Entity(tableName = "music_artist_cross_ref", primaryKeys = ["music_id", "artist_id"])
data class MusicArtistCrossRef(
    @ColumnInfo(name = "music_id")
    val musicId: Long,
    @ColumnInfo(name = "artist_id")
    val artistId: Long,
    /**
     * 歌曲在歌手中的位置
     */
    val index: Int
)
