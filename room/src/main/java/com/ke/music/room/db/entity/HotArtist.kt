package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 热门歌手
 */
@Entity("hot_artist")
data class HotArtist(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo("artist_id")
    val artistId: Long,
    val name: String,
    val avatar: String
)
