package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MvAllResponse(
    val data: List<Mv>,
    val hasMore: Boolean,
    val code: Int
)


@JsonClass(generateAdapter = true)
data class Mv(
    val id: Long,
    val cover: String,
    val name: String,
    val playCount: Int,
    val artistName: String,
    val duration: Int,
    val artists: List<MvArtist>
)


@JsonClass(generateAdapter = true)
data class MvArtist(
    val id: Long,
    val name: String
)