package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("new_album_cross_ref")
data class NewAlbumCrossRef(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("album_id")
    val albumId: Long,
    val area: String,
)
