package com.ke.compose.music.ui.playlist_category

import com.ke.music.api.response.PlaylistCategory

sealed interface PlaylistCategoryUiState {
    object Loading : PlaylistCategoryUiState

    data class Detail(val list: List<PlaylistCategory>) : PlaylistCategoryUiState

    object Error : PlaylistCategoryUiState
}