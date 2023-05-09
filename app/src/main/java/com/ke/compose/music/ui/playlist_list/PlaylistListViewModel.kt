package com.ke.compose.music.ui.playlist_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.compose.music.MainApplication
import com.ke.compose.music.domain.Result
import com.ke.compose.music.ui.playlist.GetUserPlaylistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistListViewModel @Inject constructor(
    private val getUserPlaylistUseCase: GetUserPlaylistUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PlaylistListUiState>(PlaylistListUiState.Loading)

    internal val uiState: StateFlow<PlaylistListUiState>
        get() = _uiState


    init {
        loadPlaylist()
    }

    internal fun loadPlaylist() {
        viewModelScope.launch {
            _uiState.value = PlaylistListUiState.Loading

            when (val result = getUserPlaylistUseCase(Unit)) {
                is Result.Error -> {
                    _uiState.value = PlaylistListUiState.Error
                }

                is Result.Success -> {
                    _uiState.value = PlaylistListUiState.Data(result.data.filter {
                        it.creator.userId == MainApplication.currentUserId
                    })
                }
            }
        }
    }
}