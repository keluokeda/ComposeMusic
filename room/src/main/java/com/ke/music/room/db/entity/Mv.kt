package com.ke.music.room.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ke.music.common.entity.IMv
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "mv")
data class Mv(
    @PrimaryKey
    override val id: Long,
    override val name: String,
    override val image: String,
    @ColumnInfo("play_count")
    override val playCount: Int,
    override val duration: Int,
    @ColumnInfo("publish_time")
    override val publishTime: String,
    @ColumnInfo("artist_name")
    override val artistName: String,
) : Parcelable, IMv
