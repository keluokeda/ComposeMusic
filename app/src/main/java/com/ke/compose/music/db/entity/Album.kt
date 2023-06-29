package com.ke.compose.music.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "album")
data class Album(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("album_id")
    val albumId: Long,
    val name: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String
)