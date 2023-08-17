package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.common.repository.DownloadRepository
import com.ke.music.common.repository.SongRepository
import com.ke.music.room.db.entity.Download
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DownloadMusicListUseCase @Inject constructor(
//    private val downloadingMusicDao: DownloadingMusicDao,
    private val httpService: HttpService,
    private val musicRepository: SongRepository,
    private val downloadRepository: DownloadRepository,
) :
    UseCase<List<Long>, Unit>(Dispatchers.IO) {
    override suspend fun execute(parameters: List<Long>) {

        val needQueryDetailMusicIdList = mutableListOf<Long>()

        parameters.forEach { id ->
            val music = musicRepository.findById(id)
            if (music == null) {
                //本地音乐信息不存在
                needQueryDetailMusicIdList.add(id)
            }
        }

        if (needQueryDetailMusicIdList.isNotEmpty()) {
            val ids = needQueryDetailMusicIdList.joinToString(",") { it.toString() }
            val songs = httpService.getSongsDetail(ids).songs
            musicRepository.saveSongs(songs)
        }


        downloadRepository.download(parameters, Download.SOURCE_TYPE_MUSIC)
    }
}