package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.common.repository.SongRepository
import com.ke.music.room.db.dao.ArtistMusicCrossRefDao
import com.ke.music.room.db.entity.ArtistMusicCrossRef
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LoadArtistMusicListUseCase @Inject constructor(
    private val httpService: HttpService,
    private val artistMusicCrossRefDao: ArtistMusicCrossRefDao,
    private val musicRepository: SongRepository,
) :
    UseCase<Long, Pair<String, String>>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long): Pair<String, String> {
        val response = httpService.getArtists(parameters)

        musicRepository.saveSongs(response.hotSongs)

        artistMusicCrossRefDao.clearByArtistId(parameters)
        artistMusicCrossRefDao.insertAll(
            response.hotSongs.map {
                ArtistMusicCrossRef(0, parameters, it.id)
            }
        )

        return response.artist.name to response.artist.avatar

    }
}