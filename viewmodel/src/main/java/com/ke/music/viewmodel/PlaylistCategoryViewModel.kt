package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.api.response.PlaylistCategory
import com.ke.music.common.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistCategoryViewModel @Inject constructor(private val getPlaylistCategoryListUseCase: com.ke.music.common.domain.GetPlaylistCategoryListUseCase) :
    ViewModel() {
    private val _uiState =
        MutableStateFlow<PlaylistCategoryUiState>(PlaylistCategoryUiState.Loading)

    val uiState: StateFlow<PlaylistCategoryUiState>
        get() = _uiState

    init {
        loadContent()
    }

    fun loadContent() {
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

sealed interface PlaylistCategoryUiState {
    object Loading : PlaylistCategoryUiState

    data class Detail(val list: List<PlaylistCategory>) : PlaylistCategoryUiState

    object Error : PlaylistCategoryUiState
}