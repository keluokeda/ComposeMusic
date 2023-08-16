package com.ke.music.common.repository

import kotlinx.coroutines.flow.Flow

interface CurrentUserRepository {


    /**
     * 当前用户的id流
     */
    val userIdFlow: Flow<Long>

    /**
     * 已阻塞方式获取当前用户id
     */
    suspend fun userId(): Long

    /**
     * 直接获取当前用户id
     */
    val userId: Long
}