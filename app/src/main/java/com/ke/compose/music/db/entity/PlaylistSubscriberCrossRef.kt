package com.ke.compose.music.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "playlist_subscriber_cross_ref", primaryKeys = ["playlist_id", "user_id"])
data class PlaylistSubscriberCrossRef(
    @ColumnInfo(name = "playlist_id")
    val playlistId: Long,
    @ColumnInfo(name = "user_id")
    val userId: Long,
    val index: Int
)
