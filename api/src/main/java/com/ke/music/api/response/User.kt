package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val userId: Long,
    val signature: String? = "",
    val nickname: String,
    val followed: Boolean,
    val avatarUrl: String
)
