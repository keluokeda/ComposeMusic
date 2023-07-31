package com.ke.compose.music.ui.recommend_songs

import com.ke.compose.music.db.dao.RecommendSongDao
import com.ke.compose.music.db.entity.RecommendSong
import com.ke.compose.music.domain.UseCase
import com.ke.compose.music.repository.MusicRepository
import com.ke.compose.music.repository.UserIdRepository
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LoadRecommendSongsUseCase @Inject constructor(
    private val httpService: HttpService,
    private val userIdRepository: UserIdRepository,
    private val recommendSongDao: RecommendSongDao,
    private val musicRepository: MusicRepository
) :
    UseCase<Unit, Unit>(Dispatchers.IO) {

    override suspend fun execute(parameters: Unit) {
        val songs = httpService.recommendSongs().data?.dailySongs ?: return
        val currentUserId = userIdRepository.userId()

        musicRepository.saveMusicListToRoom(songs = songs)
        recommendSongDao.clearAll(currentUserId)
        recommendSongDao.insertAll(songs.map {
            RecommendSong(0, it.id, currentUserId)
        })
    }
}