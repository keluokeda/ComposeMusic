package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.api.response.PlaylistCategory
import com.ke.music.common.domain.GetPlaylistCategoryListUseCase
import javax.inject.Inject

internal class GetPlaylistCategoryListUseCaseImpl @Inject constructor(private val httpService: HttpService) :
    UseCase<Unit, List<PlaylistCategory>>(),
    GetPlaylistCategoryListUseCase {

    override suspend fun execute(parameters: Unit): List<PlaylistCategory> {
        val response = httpService.getPlaylistCategory()

        return listOf(response.all) + response.sub
    }
}