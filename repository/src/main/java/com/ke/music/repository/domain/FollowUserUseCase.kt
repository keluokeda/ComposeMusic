package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * 关注或取关用户
 */
class FollowUserUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Pair<Long, Boolean>, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: Pair<Long, Boolean>): Boolean {
        return httpService.followUser(parameters.first, if (parameters.second) 1 else 2).success
    }
}