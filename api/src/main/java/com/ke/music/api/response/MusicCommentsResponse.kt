package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MusicCommentsResponse(
    val comments: List<Comment>,
    val more: Boolean
)
