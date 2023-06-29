package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaylistCategoryResponse(
    val all: PlaylistCategory,
    val sub: List<PlaylistCategory>
)

@JsonClass(generateAdapter = true)
data class PlaylistCategory(
    val name: String,
    val hot: Boolean
)
