package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "new_album")
data class NewAlbum(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo("album_id")
    val albumId: Long,

    val name: String,

    val subtitle: String,

    val image: String,

    /**
     * 地区
     */
    val area: String
)
