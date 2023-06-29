package com.ke.compose.music.ui.playlist_category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.compose.music.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistCategoryViewModel @Inject constructor(private val getPlaylistCategoryListUseCase: GetPlaylistCategoryListUseCase) :
    ViewModel() {
    private val _uiState =
        MutableStateFlow<PlaylistCategoryUiState>(PlaylistCategoryUiState.Loading)

    internal val uiState: StateFlow<PlaylistCategoryUiState>
        get() = _uiState

    init {
        loadContent()
    }

    internal fun loadContent() {
        viewModelScope.launch {
            _uiState.value = PlaylistCategoryUiState.Loading
            when (val result = getPlaylistCategoryListUseCase(Unit)) {
                is Result.Error -> {
                    _uiState.value = PlaylistCategoryUiState.Error
                }

                is Result.Success -> {
                    _uiState.value = PlaylistCategoryUiState.Detail(result.data)
                }
            }
        }
    }
}