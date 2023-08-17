package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.DownloadSongListUseCase
import com.ke.music.common.entity.DownloadSourceType
import com.ke.music.common.repository.DownloadRepository
import com.ke.music.common.repository.SongRepository
import javax.inject.Inject

internal class DownloadSongListUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val songRepository: SongRepository,
    private val downloadRepository: DownloadRepository,
) : UseCase<List<Long>, Unit>(), DownloadSongListUseCase {
    override suspend fun execute(parameters: List<Long>) {
        val needQueryDetailMusicIdList = mutableListOf<Long>()

        parameters.forEach { id ->
            val music = songRepository.findById(id)
            if (music == null) {
                //本地音乐信息不存在
                needQueryDetailMusicIdList.add(id)
            }
        }

        if (needQueryDetailMusicIdList.isNotEmpty()) {
            val ids = needQueryDetailMusicIdList.joinToString(",") { it.toString() }
            val songs = httpService.getSongsDetail(ids).songs
            songRepository.saveSongs(songs)
        }


        downloadRepository.download(parameters, DownloadSourceType.Song)
    }
}