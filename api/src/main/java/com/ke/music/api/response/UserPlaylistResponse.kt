package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserPlaylistResponse(
    val playlist: List<Playlist>,
    val more: Boolean,
    val code: Int
)
