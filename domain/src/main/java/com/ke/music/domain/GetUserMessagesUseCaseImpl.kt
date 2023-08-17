package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.api.response.PrivateMessage
import com.ke.music.common.domain.GetUserMessagesUseCase
import javax.inject.Inject

internal class GetUserMessagesUseCaseImpl @Inject constructor(private val httpService: HttpService) :
    UseCase<Unit, List<PrivateMessage>>(),
    GetUserMessagesUseCase {

    override suspend fun execute(parameters: Unit): List<PrivateMessage> {
        return httpService.getPrivateMessageList().list

    }
}