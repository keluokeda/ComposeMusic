package com.ke.music.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PrivateMessageResponse(
    @Json(name = "msgs")
    val list: List<PrivateMessage>,
    val more: Boolean,
    val code: Int,
    @Json(name = "newMsgCount")
    val newCount: Int
)

@JsonClass(generateAdapter = true)
data class PrivateMessage(
    @Json(name = "lastMsg")
    val lastMessage: String,
    @Json(name = "lastMsgTime")
    val time: Long,
    val fromUser: User,
    val toUser: User,
    @Json(name = "lastMsgId")
    val id: Long? = 0
)