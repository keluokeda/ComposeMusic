package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * 用户关注歌手表
 */
@Entity("user_artist_cross_ref", primaryKeys = ["user_id", "artist_id"])
data class UserArtistCrossRef(
    @ColumnInfo("user_id")
    val userId: Long,
    @ColumnInfo("artist_id")
    val artistId: Long
)
