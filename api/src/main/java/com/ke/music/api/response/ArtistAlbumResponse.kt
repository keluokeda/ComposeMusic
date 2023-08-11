package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistAlbumResponse(
    val hotAlbums: List<Album>,
    val more: Boolean,
    val code: Int
)
