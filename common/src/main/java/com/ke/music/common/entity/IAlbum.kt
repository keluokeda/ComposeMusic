package com.ke.music.common.entity

import android.os.Parcelable

/**
 * 专辑
 */
interface IAlbum : Parcelable {
    val albumId: Long
    val name: String
    val image: String
    val key: Long
}