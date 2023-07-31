package com.ke.music.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaylistTopResponse(
    val playlists: List<Playlist>,
    val more: Boolean,
    @Json(name = "cat")
    val category: String,
)
