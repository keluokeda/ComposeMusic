package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.CreateQRUrlUseCase
import javax.inject.Inject

internal class CreateQRUrlUseCaseImpl @Inject constructor(private val httpService: HttpService) :
    UseCase<Unit, Pair<String, String>>(), CreateQRUrlUseCase {

    override suspend fun execute(parameters: Unit): Pair<String, String> {
        val key = httpService.createQRKey().data!!.unikey!!
        val url = httpService.createQRUrl(key).data!!.qrurl!!

        return key to url
    }
}