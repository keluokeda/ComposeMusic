package com.ke.music.repository

import com.ke.music.common.repository.UserRepository
import com.ke.music.room.db.dao.UserDao
import com.ke.music.room.db.entity.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(private val userDao: UserDao) : UserRepository {

    private suspend fun saveUserList(list: List<User>) {
        userDao.insertAll(list)
    }

    override suspend fun saveUsers(list: List<com.ke.music.api.response.User>) {
        saveUserList(list.map {
            User(it.userId, it.nickname, it.avatarUrl, it.signature)
        })
    }

    override suspend fun findById(userId: Long) = userDao.findById(userId)
}