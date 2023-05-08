package com.ke.music.api.response

import com.ke.music.api.entity.UsersProvider
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaylistSubscribersResponse(
    val subscribers: List<User>,
    val more: Boolean
) : UsersProvider {
    override fun users(): List<User> {
        return subscribers
    }

    override fun more(): Boolean {
        return more
    }

}
