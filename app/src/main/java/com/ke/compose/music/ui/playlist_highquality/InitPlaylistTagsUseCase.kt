package com.ke.compose.music.ui.playlist_highquality

import com.ke.music.api.HttpService
import com.ke.music.repository.domain.UseCase
import com.ke.music.room.db.dao.PlaylistTagDao
import com.ke.music.room.db.entity.PlaylistTag
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class InitPlaylistTagsUseCase @Inject constructor(
    private val httpService: HttpService,
    private val playlistTagDao: PlaylistTagDao
) :
    UseCase<Unit, Unit>(Dispatchers.IO) {

    override suspend fun execute(parameters: Unit) {
        val currentTags = playlistTagDao.getAllTags().map { it.tagId }
        val serverTags = httpService.getPlaylistTags().tags

        val insertList = serverTags.mapIndexed { index, playlistTag ->
            PlaylistTag(
                tagId = playlistTag.id,
                name = playlistTag.name,
                hot = playlistTag.hot,
                index = index
            )
        }.filter {
            !currentTags.contains(it.tagId)
        }

        playlistTagDao.insertAll(insertList)
    }
}