package com.ke.compose.music.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music")
data class Music(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("music_id")
    val musicId: Long,
    val name: String,
    @ColumnInfo(name = "album_id")
    val albumId: Long,
    /**
     * mv的id，如果是0表示没有
     */
    val mv: Long
)