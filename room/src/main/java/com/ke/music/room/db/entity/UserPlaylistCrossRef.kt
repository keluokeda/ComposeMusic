package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * 用户和歌单之间的关系
 */
@Entity(tableName = "user_playlist_cross_ref", primaryKeys = ["user_id", "playlist_id"])
data class UserPlaylistCrossRef(
    @ColumnInfo(name = "user_id")
    val userId: Long,
    @ColumnInfo(name = "playlist_id")
    val playlistId: Long,
    val index: Int
)
