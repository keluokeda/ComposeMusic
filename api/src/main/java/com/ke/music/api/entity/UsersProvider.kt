package com.ke.music.api.entity

import com.ke.music.api.response.User

interface UsersProvider {
    fun users(): List<User>

    fun more(): Boolean
}