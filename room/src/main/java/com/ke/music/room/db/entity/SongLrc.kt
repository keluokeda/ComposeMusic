package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("song_lrc")
data class SongLrc(
    @PrimaryKey
    @ColumnInfo("song_id")
    val songId: Long,
    val lrc: String
)
