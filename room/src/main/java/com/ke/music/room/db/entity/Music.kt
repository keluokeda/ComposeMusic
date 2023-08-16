package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ke.music.common.entity.ISong

@Entity(tableName = "music")
data class Music(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo("music_id")
    val musicId: Long,
    override val name: String,
    @ColumnInfo(name = "album_id")
    override val albumId: Long,
    /**
     * mv的id，如果是0表示没有
     */
    override val mv: Long,
) : ISong {
    override val id: Long
        get() = musicId
}