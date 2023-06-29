package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HighQualityPlaylistsResponse(
    val playlists: List<Playlist>,
    val more: Boolean,
)
