package com.ke.music.api.response

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class MusicUrlResponse(
    val data: List<SongUrl>
)

@JsonClass(generateAdapter = true)
data class MusicDownloadUrlResponse(
    val data: SongUrl?
)

@JsonClass(generateAdapter = true)
data class SongUrl(
    val url: String?
)