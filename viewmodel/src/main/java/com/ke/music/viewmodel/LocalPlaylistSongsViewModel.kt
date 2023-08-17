package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.common.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocalPlaylistSongsViewModel @Inject constructor(
    private val musicRepository: SongRepository,
) : ViewModel() {
    val songs = musicRepository.getLocalPlaylistSongList().stateIn(
        viewModelScope, SharingStarted.Eagerly,
        emptyList()
    )

    fun removeSong(songId: Long) {
        viewModelScope.launch {
            musicRepository.deleteLocalPlaylistSong(songId)
        }
    }
}