package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ke.music.common.entity.IAlbum
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "album")
data class Album(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("album_id")
    override val albumId: Long,
    override val name: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,
) : IAlbum {
    override val key: Long
        get() = albumId

    override val image: String
        get() = imageUrl
}