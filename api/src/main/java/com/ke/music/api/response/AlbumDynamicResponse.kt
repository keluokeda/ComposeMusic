package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AlbumDynamicResponse(
    val isSub: Boolean
)
