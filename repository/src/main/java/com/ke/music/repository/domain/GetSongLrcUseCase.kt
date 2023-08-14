package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.repository.MusicRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetSongLrcUseCase @Inject constructor(
    private val httpService: HttpService,
    private val musicRepository: MusicRepository
) : UseCase<Pair<Long, Boolean>, String>(Dispatchers.IO) {

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