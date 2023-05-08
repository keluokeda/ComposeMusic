package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserFollowsResponse(
    val more: Boolean,
    val follow: List<User>
)
