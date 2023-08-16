package com.ke.music.common.repository

import com.ke.music.api.response.User
import com.ke.music.common.entity.IUser

interface UserRepository {
    suspend fun saveUsers(list: List<User>)
    suspend fun findById(userId: Long): IUser?
}