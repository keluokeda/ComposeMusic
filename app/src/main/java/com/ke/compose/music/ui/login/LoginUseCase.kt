package com.ke.compose.music.ui.login

import android.content.Context
import com.ke.compose.music.domain.UseCase
import com.ke.compose.music.setUserId
import com.ke.music.api.HttpService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val httpService: HttpService,
    @ApplicationContext private val context: Context
) :
    UseCase<String, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: String): Boolean {
        if (httpService.checkLoginByKey(parameters).code == 803) {
            context.setUserId(httpService.loginStatus().data?.profile?.userId ?: 0L)
            return true
        }

        return false
    }
}