package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.ShareResourceUseCase
import com.ke.music.common.entity.ShareRequest
import com.ke.music.common.entity.ShareType
import javax.inject.Inject

internal class ShareResourceUseCaseImpl @Inject constructor(private val httpService: HttpService) :
    UseCase<ShareRequest, Boolean>(), ShareResourceUseCase {

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