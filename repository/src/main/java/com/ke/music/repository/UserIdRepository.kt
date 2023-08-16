package com.ke.music.repository

import android.content.Context
import com.ke.music.common.repository.CurrentUserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserIdRepository @Inject constructor(@ApplicationContext private val context: Context) :
    CurrentUserRepository {

    override val userIdFlow: Flow<Long>
        get() = context.userIdFlow

    override suspend fun userId(): Long = context.getUserId()

    override val userId: Long
        get() = runBlocking {
            context.getUserId()
        }
}