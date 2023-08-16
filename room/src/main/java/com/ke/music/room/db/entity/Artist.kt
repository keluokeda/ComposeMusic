package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ke.music.common.entity.IArtist
import kotlinx.parcelize.Parcelize

/**
 * 歌手
 */
@Entity(tableName = "artist")
@Parcelize
data class Artist(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("artist_id")
    override val artistId: Long,
    override val name: String,
    override val avatar: String? = null,
) : IArtist {


    override val key: Long
        get() = artistId
}