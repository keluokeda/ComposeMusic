package com.ke.compose.music.ui.share

import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ShareResourceUseCase @Inject constructor(private val httpService: HttpService) :
    UseCase<ShareRequest, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: ShareRequest): Boolean {
        val ids = parameters.users.joinToString(",") { it.toString() }
        return when (parameters.shareType) {
            ShareType.Playlist -> httpService.sendPlaylistToUsers(
                ids,
                parameters.id,
                parameters.message
            ).success

            ShareType.Album -> httpService.sendAlbumToUsers(
                ids,
                parameters.id,
                parameters.message
            ).success

            ShareType.Song -> httpService.sendSongToUsers(
                ids,
                parameters.id,
                parameters.message
            ).success
        }


    }
}