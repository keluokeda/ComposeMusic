package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.api.response.UserDetailResponse
import com.ke.music.common.domain.GetCurrentUserDetailUseCase
import com.ke.music.common.repository.CurrentUserRepository
import javax.inject.Inject

internal class GetCurrentUserDetailUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val currentUserRepository: CurrentUserRepository,
) :
    UseCase<Unit, UserDetailResponse>(),
    GetCurrentUserDetailUseCase {

    override suspend fun execute(parameters: Unit): UserDetailResponse {
        return httpService.getUserDetail(currentUserRepository.userId())

    }
}