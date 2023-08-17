package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.LoginUseCase
import com.ke.music.common.repository.CurrentUserRepository
import javax.inject.Inject

internal class LoginUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val currentUserRepository: CurrentUserRepository,
) :
    UseCase<String, Boolean>(), LoginUseCase {
    override suspend fun execute(parameters: String): Boolean {
        if (httpService.checkLoginByKey(parameters).code == 803) {
            currentUserRepository.setUserId(httpService.loginStatus().data?.profile?.userId ?: 0L)
            return true
        }

        return false
    }
}