package com.ke.music.common.entity

import android.os.Parcelable

interface IComment : Parcelable {
    val commentId: Long
    val user: IUser
    val content: String
    val timeString: String
    val ip: String
    val likedCount: Int
    val replyCount: Int
    val liked: Boolean
    val key: Long
}