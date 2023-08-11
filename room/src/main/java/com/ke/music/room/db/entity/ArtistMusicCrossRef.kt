package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("artist_music_cross_ref")
data class ArtistMusicCrossRef(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("artist_id")
    val artistId: Long,
    @ColumnInfo("music_id")
    val musicId: Long
)
