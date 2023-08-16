package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hot_artist_cross_ref")
data class HotArtistCrossRef(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("artist_id")
    val artistId: Long,
    val area: Int,
    val type: Int,
)
