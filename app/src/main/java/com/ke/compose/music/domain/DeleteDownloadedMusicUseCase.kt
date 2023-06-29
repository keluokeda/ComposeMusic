package com.ke.compose.music.domain

import com.ke.compose.music.repository.DownloadRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DeleteDownloadedMusicUseCase @Inject constructor(private val downloadRepository: DownloadRepository) :
    UseCase<Long, Boolean>(Dispatchers.IO) {

    override suspend fun execute(parameters: Long): Boolean {
        return downloadRepository.deleteDownloadedMusic(parameters)
    }
}