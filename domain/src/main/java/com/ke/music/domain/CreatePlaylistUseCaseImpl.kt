package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.CreatePlaylistUseCase
import javax.inject.Inject

internal class CreatePlaylistUseCaseImpl @Inject constructor(private val httpService: HttpService) :
    UseCase<Pair<String, Boolean>, Boolean>(), CreatePlaylistUseCase {

    override suspend fun execute(parameters: Pair<String, Boolean>): Boolean {
        return httpService.createPlaylist(parameters.first).code == 200
    }
}