package com.ke.compose.music.ui.playlist_highquality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.room.db.dao.PlaylistTagDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistHighqualityViewModel @Inject constructor(
    private val playlistTagDao: PlaylistTagDao,
    private val initPlaylistTagsUseCase: InitPlaylistTagsUseCase
) :
    ViewModel() {

    internal val tags = playlistTagDao.getAll()
        .stateIn(viewModelScope, started = SharingStarted.Eagerly, initialValue = emptyList())

    init {
        viewModelScope.launch {
            initPlaylistTagsUseCase(Unit)
        }
    }
}