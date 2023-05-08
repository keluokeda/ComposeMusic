package com.ke.compose.music.ui.login

import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CreateQRUrlUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Unit, Pair<String, String>>(Dispatchers.IO) {

    override suspend fun execute(parameters: Unit): Pair<String, String> {
        val key = httpService.createQRKey().data!!.unikey!!
        val url = httpService.createQRUrl(key).data!!.qrurl!!

        return key to url
    }
}