package com.ke.compose.music.domain

import com.ke.music.api.HttpService
import com.ke.music.api.response.UserDetailResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Long, UserDetailResponse>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long): UserDetailResponse {
        return httpService.getUserDetail(parameters)
    }
}