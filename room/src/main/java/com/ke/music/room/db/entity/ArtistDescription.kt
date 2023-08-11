package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("artist_description")
data class ArtistDescription(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("artist_id")
    val artistId: Long,
    val title: String,
    val content: String
)
