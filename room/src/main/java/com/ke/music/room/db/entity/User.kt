package com.ke.music.room.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ke.music.common.entity.IUser
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user")
data class User(
    @PrimaryKey
    val id: Long,
    /**
     * 名称
     */
    override val name: String,
    /**
     * 头像
     */
    override val avatar: String,
    /**
     * 个性签名
     */
    override val signature: String?,
) : IUser {
    override val userId: Long
        get() = id

    override val key: Long
        get() = id
}
