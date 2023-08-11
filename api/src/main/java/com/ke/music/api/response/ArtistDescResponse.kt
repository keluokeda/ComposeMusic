package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistDescResponse(
    val briefDesc: String,
    val introduction: List<ArtistIntroduce>,
    val code: Int
)

@JsonClass(generateAdapter = true)
data class ArtistIntroduce(
    val ti: String,
    val txt: String
)
