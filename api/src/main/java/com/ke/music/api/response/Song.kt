package com.ke.music.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Song(
    val id: Long,
    val name: String,
    @Json(name = "al")
    val album: Album,
    @Json(name = "ar")
    val singers: List<Singer>,
    /**
     * mv的id，如果是0表示没有
     */
    val mv: Long
) {
    val subTitle: String
        get() = "${singers.firstOrNull()?.name}-${album.name}"
}

/**
 * 专辑
 */
@JsonClass(generateAdapter = true)
data class Album(
    val id: Long,
    val name: String,
    @Json(name = "picUrl")
    val imageUrl: String
)

/**
 * 歌手
 */
@JsonClass(generateAdapter = true)
data class Singer(
    val id: Long,
    val name: String
)
