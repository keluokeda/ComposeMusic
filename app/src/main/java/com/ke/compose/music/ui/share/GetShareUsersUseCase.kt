package com.ke.compose.music.ui.share

import com.ke.compose.music.MainApplication
import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import com.ke.music.api.response.User
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetShareUsersUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Unit, List<User>>(Dispatchers.IO) {
    override suspend fun execute(parameters: Unit): List<User> {
        return httpService.getUserFollows(MainApplication.currentUserId, 100, 0).follow
    }
}