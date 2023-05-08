package com.ke.compose.music.ui.login

import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<String, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: String): Boolean {
        return httpService.checkLoginByKey(parameters).code == 803
    }
}