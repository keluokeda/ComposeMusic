package com.ke.music.room.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ke.music.common.entity.IArtistDescription
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity("artist_description")
data class ArtistDescription(
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,
    @ColumnInfo("artist_id")
    override val artistId: Long,
    override val title: String,
    override val content: String,
) : Parcelable, IArtistDescription
