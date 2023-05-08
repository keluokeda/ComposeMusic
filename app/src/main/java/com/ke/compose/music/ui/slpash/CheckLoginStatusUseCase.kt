package com.ke.compose.music.ui.slpash

import com.ke.compose.music.MainApplication
import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CheckLoginStatusUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Unit, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: Unit): Boolean {
        val response = httpService.loginStatus()

        val userId = response.data?.profile?.userId
        if (userId != null) {
            MainApplication.currentUserId = userId
        }

        return userId != null
    }
}