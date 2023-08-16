package com.ke.music.common.entity

import android.os.Parcelable

interface IUser : Parcelable {
    val userId: Long

    /**
     * 名称
     */
    val name: String

    /**
     * 头像
     */
    val avatar: String

    /**
     * 个性签名
     */
    val signature: String?

    /**
     * 唯一标识
     */
    val key: Long
}