package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumResponse(
    val songs: List<Song>,
    val album: AlbumData
)

@JsonClass(generateAdapter = true)
data class AlbumData(
    val name: String,
    val description: String?,
    val id: Long,
    val artist: Singer,
    val publishTime: Long,
    val picUrl: String,
    val artists: List<Singer>,
    val company: String
)


