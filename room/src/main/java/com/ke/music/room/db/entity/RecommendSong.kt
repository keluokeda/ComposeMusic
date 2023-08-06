package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recommend_song")
data class RecommendSong(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("song_id")
    val songId: Long,
    @ColumnInfo("user_id")
    val userId: Long
)
