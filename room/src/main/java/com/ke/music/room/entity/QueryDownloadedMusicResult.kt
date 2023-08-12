package com.ke.music.room.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QueryDownloadedMusicResult(
    val musicId: Long,
    val name: String,
    val albumName: String,
    val albumImage: String,
    val path: String?,
) : Parcelable