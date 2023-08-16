package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.common.domain.DeletePlaylistUseCase
import com.ke.music.common.domain.LoadCurrentUserPlaylistUseCase
import com.ke.music.common.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val loadUserPlaylistUseCase: LoadCurrentUserPlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    playlistRepository: PlaylistRepository,
) : ViewModel() {


    val playlistList =
        playlistRepository.getCurrentUserPlaylist(true)
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    private val _refreshing = MutableStateFlow(false)
    val refreshing: StateFlow<Boolean>
        get() = _refreshing

    init {

        refresh()
    }


    fun refresh() {
        viewModelScope.launch {
            _refreshing.value = true
            loadUserPlaylistUseCase(Unit)
            _refreshing.value = false
        }
    }

    fun deletePlaylist(playlistId: Long) {

        viewModelScope.launch {
            deletePlaylistUseCase(playlistId)
        }
    }
}