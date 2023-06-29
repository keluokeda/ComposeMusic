package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaylistTagsResponse(
    val tags: List<PlaylistTag>
)

@JsonClass(generateAdapter = true)
data class PlaylistTag(
    val name: String,
    val hot: Boolean,
    val id: Long
)
