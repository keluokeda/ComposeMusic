package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDetailResponse(
    val level: Int,
    val listenSongs: Int,
    val profile: UserDetailProfile
)

@JsonClass(generateAdapter = true)
data class UserDetailProfile(
    val avatarUrl: String,
    val nickname: String,
    val userId: Long,
    val signature: String?
)
