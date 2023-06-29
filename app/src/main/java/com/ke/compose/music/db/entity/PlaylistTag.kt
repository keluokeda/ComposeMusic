package com.ke.compose.music.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_tag")
data class PlaylistTag(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "tag_id")
    val tagId: Long,
    val name: String,
    val hot: Boolean,
    val index: Int
)
