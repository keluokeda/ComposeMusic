package com.ke.compose.music.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: Long,
    /**
     * 名称
     */
    val name: String,
    /**
     * 头像
     */
    val avatar: String,
    /**
     * 个性签名
     */
    val signature: String?,
)
