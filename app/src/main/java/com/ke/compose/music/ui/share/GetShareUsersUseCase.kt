package com.ke.compose.music.ui.share

import android.content.Context
import com.ke.compose.music.domain.UseCase
import com.ke.compose.music.getUserId
import com.ke.music.api.HttpService
import com.ke.music.api.response.User
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetShareUsersUseCase @Inject constructor(
    private val httpService: HttpService,
    @ApplicationContext private val context: Context
) :
    UseCase<Unit, List<User>>(Dispatchers.IO) {
    override suspend fun execute(parameters: Unit): List<User> {
        return httpService.getUserFollows(context.getUserId(), 100, 0).follow
    }
}