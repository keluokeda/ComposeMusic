package com.ke.compose.music.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "playlist_music_cross_ref", primaryKeys = ["playlist_id", "music_id"])
data class PlaylistMusicCrossRef(
    @ColumnInfo("playlist_id")
    val playlistId: Long,
    @ColumnInfo("music_id")
    val musicId: Long,
    val index: Int
)
