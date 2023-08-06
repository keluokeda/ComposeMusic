package com.ke.music.tv.ui.downloaded.music

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.MusicRepository
import com.ke.music.repository.domain.DeleteDownloadedMusicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadedMusicViewModel @Inject constructor(
    musicRepository: MusicRepository,
    private val deleteDownloadedMusicUseCase: DeleteDownloadedMusicUseCase
) :
    ViewModel() {


    val downloadMusicList = musicRepository.getDownloadedMusics().stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList()
    )

    internal fun deleteDownloadedMusic(id: Long) {
        viewModelScope.launch {
            deleteDownloadedMusicUseCase(id)
        }
    }

}