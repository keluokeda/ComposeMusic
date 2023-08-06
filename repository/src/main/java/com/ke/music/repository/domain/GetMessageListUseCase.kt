package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.api.response.PrivateMessage
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetMessageListUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Unit, List<PrivateMessage>>(Dispatchers.IO) {

    override suspend fun execute(parameters: Unit): List<PrivateMessage> {
        return httpService.getPrivateMessageList().list
    }
}