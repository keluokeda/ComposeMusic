package com.ke.compose.music.ui.playlist

import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * 删除歌单
 */
class DeletePlaylistUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<Long, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long): Boolean {
        return httpService.deletePlaylist(parameters).success
    }
}