package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * 专辑歌手中间表
 */
@Entity("album_artist_cross_ref", primaryKeys = ["album_id", "artist_id"])
data class AlbumArtistCrossRef(
    @ColumnInfo("album_id")
    val albumId: Long,
    @ColumnInfo("artist_id")
    val artistId: Long,
    val index: Int
)
