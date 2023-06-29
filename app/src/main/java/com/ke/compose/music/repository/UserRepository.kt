package com.ke.compose.music.repository

import com.ke.compose.music.db.dao.UserDao
import com.ke.compose.music.db.entity.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun saveUserList(list: List<User>) {
        userDao.insertAll(list)
    }

    suspend fun saveUsers(list: List<com.ke.music.api.response.User>) {
        saveUserList(list.map {
            User(it.userId, it.nickname, it.avatarUrl, it.signature)
        })
    }

    suspend fun findById(userId: Long) = userDao.findById(userId)
}