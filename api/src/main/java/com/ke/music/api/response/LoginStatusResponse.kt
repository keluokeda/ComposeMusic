package com.ke.music.api.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginStatusResponse(
    val data: LoginStatusData? = null
)

@JsonClass(generateAdapter = true)
data class LoginStatusData(
    val profile: LoginStatusProfile? = null
)

@JsonClass(generateAdapter = true)
data class LoginStatusProfile(
    val userId: Long
)
