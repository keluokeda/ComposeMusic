package com.ke.compose.music.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * 用户关注专辑表
 */
@Entity("user_album_cross_ref", primaryKeys = ["user_id", "album_id"])
data class UserAlbumCrossRef(
    @ColumnInfo("user_id")
    val userId: Long,
    @ColumnInfo("album_id")
    val albumId: Long,
//    @ColumnInfo("created_time")
//    val createdTime: Long = System.currentTimeMillis()
    val index: Int = 0
)
