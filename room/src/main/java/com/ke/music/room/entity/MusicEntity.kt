package com.ke.music.room.entity

import com.ke.music.room.db.entity.Album
import com.ke.music.room.db.entity.Artist


data class MusicEntity(
    val musicId: Long,
    val name: String,
    val mv: Long,
    val album: Album,
    val artists: List<Artist>,
    /**
     * 下载状态
     */
    val downloadStatus: Int?

) {
    val subTitle: String
        get() = "${artists.joinToString("/") { it.name }} ${album.name}"


}
