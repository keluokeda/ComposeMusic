package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mv")
data class Mv(
    @PrimaryKey
    val id: Long,
    val name: String,
    val image: String,
    @ColumnInfo("play_count")
    val playCount: Int,
    val duration: Int,
    @ColumnInfo("publish_time")
    val publishTime: String,
    @ColumnInfo("artist_name")
    val artistName: String
)
