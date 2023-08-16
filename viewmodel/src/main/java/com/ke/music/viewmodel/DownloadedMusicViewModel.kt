package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.common.repository.DownloadRepository
import com.ke.music.common.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadedMusicViewModel @Inject constructor(
//    musicRepository: MusicRepository,
//    private val deleteDownloadedMusicUseCase: DeleteDownloadedMusicUseCase,
    songRepository: SongRepository,
    private val downloadRepository: DownloadRepository,
) :
    ViewModel() {


    val downloadedMusicList = songRepository.getDownloadedSongs().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    fun deleteDownloadedMusic(id: Long) {
        viewModelScope.launch {
//            deleteDownloadedMusicUseCase(id)
            downloadRepository.deleteDownloadedSong(id)
        }
    }

}