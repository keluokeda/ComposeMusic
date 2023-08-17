package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.api.response.User
import com.ke.music.common.domain.GetShareUsersUseCase
import com.ke.music.common.repository.CurrentUserRepository
import javax.inject.Inject

internal class GetShareUsersUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val currentUserRepository: CurrentUserRepository,
) : UseCase<Unit, List<User>>(), GetShareUsersUseCase {

    override suspend fun execute(parameters: Unit): List<User> {
        return httpService.getUserFollows(currentUserRepository.userId(), 100, 0).follow
    }
}