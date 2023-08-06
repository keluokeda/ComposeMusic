package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.room.db.dao.PlaylistMusicCrossRefDao
import com.ke.music.room.db.entity.PlaylistMusicCrossRef
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class AddOrRemoveSongsToPlaylistUseCase @Inject constructor(
    private val httpService: HttpService,
    private val playlistMusicCrossRefDao: PlaylistMusicCrossRefDao
) :
    UseCase<AddOrRemoveSongsToPlaylistRequest, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: AddOrRemoveSongsToPlaylistRequest): Boolean {


        if (parameters.add) {
            playlistMusicCrossRefDao.insertAll(
                parameters.songIds.mapIndexed { index, id ->
                    PlaylistMusicCrossRef(parameters.playlistId, id, index)
                }
            )
        } else {
            parameters.songIds.forEach { id ->
                playlistMusicCrossRefDao.deleteByPlaylistIdAndMusicId(
                    parameters.playlistId,
                    id
                )
            }
        }

        Logger.d("操作结果  $parameters")


        val songIds = parameters.songIds.joinToString(",") { it.toString() }
        httpService.addOrRemoveSongsToPlaylist(
            if (parameters.add) "add" else "del",
            parameters.playlistId,
            songIds
        )



        return true
    }
}

data class AddOrRemoveSongsToPlaylistRequest(
    val playlistId: Long,
    val songIds: List<Long>,
    val add: Boolean
)