package com.ke.music.common.entity

import android.os.Parcelable

/**
 * 歌手
 */
interface IArtist : Parcelable {
    val artistId: Long
    val name: String
    val avatar: String?

    /**
     * 唯一标识
     */
    val key: Long
}