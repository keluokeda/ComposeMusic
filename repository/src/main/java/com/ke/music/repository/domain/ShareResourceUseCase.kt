package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.repository.entity.ShareRequest
import com.ke.music.repository.entity.ShareType
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