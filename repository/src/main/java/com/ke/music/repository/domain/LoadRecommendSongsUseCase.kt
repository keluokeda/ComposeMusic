package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.common.repository.CurrentUserRepository
import com.ke.music.common.repository.SongRepository
import com.ke.music.room.db.dao.RecommendSongDao
import com.ke.music.room.db.entity.RecommendSong
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LoadRecommendSongsUseCase @Inject constructor(
    private val httpService: HttpService,
    private val userIdRepository: CurrentUserRepository,
    private val recommendSongDao: RecommendSongDao,
    private val musicRepository: SongRepository,
) :
    UseCase<Unit, Unit>(Dispatchers.IO) {

    override suspend fun execute(parameters: Unit) {
        val songs = httpService.recommendSongs().data?.dailySongs ?: return
        val currentUserId = userIdRepository.userId()

        musicRepository.saveSongs(songs)
        recommendSongDao.clearAll(currentUserId)
        recommendSongDao.insertAll(songs.map {
            RecommendSong(0, it.id, currentUserId)
        })
    }
}