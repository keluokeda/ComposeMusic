package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity("mv_artist_cross_ref", primaryKeys = ["artist_id", "mv_id"])
data class MvArtistCrossRef(
    @ColumnInfo("artist_id")
    val artistId: Long,
    @ColumnInfo("mv_id")
    val mvId: Long,
    val index: Int
)
