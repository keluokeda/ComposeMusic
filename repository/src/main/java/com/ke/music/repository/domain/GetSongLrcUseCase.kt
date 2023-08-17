package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.common.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class GetSongLrcUseCase @Inject constructor(
    private val httpService: HttpService,
    private val songRepository: SongRepository,
) : UseCase<Pair<Long, Boolean>, String>(Dispatchers.IO) {

    override suspend fun execute(parameters: Pair<Long, Boolean>): String {
        val localLrc = songRepository.getSongLrc(parameters.first)
        if (localLrc != null) {
            return localLrc
        }
        val lyric = httpService.getSongLrc(parameters.first).lrc.lyric

        songRepository.saveSongLrc(parameters.first, lyric)
        return lyric
    }
}