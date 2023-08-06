package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 歌手
 */
@Entity(tableName = "artist")
data class Artist(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("artist_id")
    val artistId: Long,
    val name: String
)