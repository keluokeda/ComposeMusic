package com.ke.music.repository.domain

import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DeleteDownloadedMusicUseCase @Inject constructor(private val downloadRepository: com.ke.music.common.repository.DownloadRepository) :
    UseCase<Long, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long): Boolean {
        return downloadRepository.deleteDownloadedSong(parameters)
    }
}