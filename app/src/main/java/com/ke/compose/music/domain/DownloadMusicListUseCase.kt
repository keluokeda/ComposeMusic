package com.ke.compose.music.domain

import com.ke.compose.music.db.entity.Download
import com.ke.compose.music.repository.DownloadRepository
import com.ke.compose.music.repository.MusicRepository
import com.ke.music.api.HttpService
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DownloadMusicListUseCase @Inject constructor(
//    private val downloadingMusicDao: DownloadingMusicDao,
    private val httpService: HttpService,
    private val musicRepository: MusicRepository,
    private val downloadRepository: DownloadRepository
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
            musicRepository.saveMusicListToRoom(songs)
        }


        downloadRepository.download(parameters, Download.SOURCE_TYPE_MUSIC)
    }
}