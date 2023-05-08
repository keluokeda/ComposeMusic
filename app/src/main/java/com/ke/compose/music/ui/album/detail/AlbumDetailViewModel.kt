package com.ke.compose.music.ui.album.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.compose.music.domain.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val getAlbumDetailUseCase: GetAlbumDetailUseCase,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    internal val id = savedStateHandle.get<Long>("id")!!

    private val _uiState = MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Loading)

    internal val uiState: StateFlow<AlbumDetailUiState>
        get() = _uiState

    init {
        loadDetail()
    }


    internal fun loadDetail() {
        viewModelScope.launch {
            _uiState.value = AlbumDetailUiState.Loading
            _uiState.value = getAlbumDetailUseCase(id).successOr(AlbumDetailUiState.Error)
        }
    }
}