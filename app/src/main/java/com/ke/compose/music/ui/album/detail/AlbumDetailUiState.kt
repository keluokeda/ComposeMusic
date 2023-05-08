package com.ke.compose.music.ui.album.detail

import com.ke.music.api.response.Song

sealed interface AlbumDetailUiState {
    object Loading : AlbumDetailUiState

    data class Detail(
        val id: Long,
        val name: String,
        val description: String?,
        val artistName: String,
        val artistId: Long,
        val image: String,
        val publishTime: String,
        val songs: List<Song>
    ) : AlbumDetailUiState

    object Error : AlbumDetailUiState
}