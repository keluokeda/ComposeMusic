package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PlaylistListViewModel @Inject constructor(
    playlistRepository: PlaylistRepository
) : ViewModel() {



    val list = playlistRepository.getCurrentUserPlaylist()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


}