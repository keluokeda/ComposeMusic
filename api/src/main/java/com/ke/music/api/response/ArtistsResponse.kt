package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistsResponse(
    val artist: Artist,
    val hotSongs: List<Song>
)
