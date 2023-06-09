package com.ke.compose.music.ui.playlist_category

import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import com.ke.music.api.response.PlaylistCategory
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetPlaylistCategoryListUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Unit, List<PlaylistCategory>>(Dispatchers.IO) {

    override suspend fun execute(parameters: Unit): List<PlaylistCategory> {
        val response = httpService.getPlaylistCategory()

        return listOf(response.all) + response.sub
    }
}