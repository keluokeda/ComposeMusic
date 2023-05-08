package com.ke.compose.music.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ke.compose.music.ui.users.UsersType

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo("user_id")
    val userId: Long,
    val name: String,
    val avatar: String,
    val signature: String,
    val followed: Boolean,
    @ColumnInfo("users_type")
    val usersType: UsersType,
    @ColumnInfo(name = "source_id")
    val sourceId: Long
)
