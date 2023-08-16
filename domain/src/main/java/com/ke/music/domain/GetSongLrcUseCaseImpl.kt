package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.GetSongLrcUseCase
import com.ke.music.common.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetSongLrcUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val musicRepository: SongRepository,
) : UseCase<Pair<Long, Boolean>, String>(Dispatchers.IO), GetSongLrcUseCase {

    override suspend fun execute(parameters: Pair<Long, Boolean>): String {
        val localLrc = musicRepository.getSongLrc(parameters.first)
        if (localLrc != null) {
            return localLrc
        }
        val lyric = httpService.getSongLrc(parameters.first).lrc.lyric

        musicRepository.saveSongLrc(parameters.first, lyric)
        return lyric
    }
}