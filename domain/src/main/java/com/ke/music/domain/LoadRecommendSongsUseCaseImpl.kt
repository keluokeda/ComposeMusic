package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.LoadRecommendSongsUseCase
import com.ke.music.common.repository.SongRepository
import javax.inject.Inject

class LoadRecommendSongsUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val songRepository: SongRepository,
) : UseCase<Unit, Unit>(), LoadRecommendSongsUseCase {

    override suspend fun execute(parameters: Unit) {
        val songs = httpService.recommendSongs().data?.dailySongs ?: return
        songRepository.saveRecommendSongs(songs)

//        musicRepository.saveMusicListToRoom(songs = songs)
//        recommendSongDao.clearAll(currentUserId)
//        recommendSongDao.insertAll(songs.map {
//            RecommendSong(0, it.id, currentUserId)
//        })
    }
}