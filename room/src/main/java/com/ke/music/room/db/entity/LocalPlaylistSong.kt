package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 本地播放列表的音乐
 */
@Entity("local_playlist_song")
data class LocalPlaylistSong(
    @PrimaryKey
    @ColumnInfo("song_id")
    val songId: Long,

    /**
     * 加入的时间
     */
    @ColumnInfo("added_time")
    val addedTime: Long = System.currentTimeMillis()
)
