package com.ke.compose.music.ui.playlist_list

import com.ke.music.api.response.Playlist

sealed interface PlaylistListUiState {
    object Loading : PlaylistListUiState

    data class Data(val list: List<Playlist>) : PlaylistListUiState

    object Error : PlaylistListUiState
}