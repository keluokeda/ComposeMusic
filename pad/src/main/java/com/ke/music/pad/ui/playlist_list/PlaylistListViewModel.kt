package com.ke.music.pad.ui.playlist_list

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

//    private val _uiState = MutableStateFlow<PlaylistListUiState>(PlaylistListUiState.Loading)
//
//    internal val uiState: StateFlow<PlaylistListUiState>
//        get() = _uiState


    val list = playlistRepository.getCurrentUserPlaylist()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


}