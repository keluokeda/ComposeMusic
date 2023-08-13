package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 音乐播放记录
 */
@Entity("song_play_record")
data class SongPlayRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("song_id")
    val songId: Long,
    /**
     * 日期
     */
    val date: Long = System.currentTimeMillis()
)
