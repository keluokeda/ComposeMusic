package com.ke.compose.music.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "top_playlist")
data class TopPlaylist(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    /**
     * 歌单id
     */
    @ColumnInfo(name = "playlist_id")
    val playlistId: Long,
    /**
     * 分类
     */
    val category: String?
)
