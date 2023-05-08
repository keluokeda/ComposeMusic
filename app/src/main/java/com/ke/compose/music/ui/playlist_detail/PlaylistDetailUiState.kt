package com.ke.compose.music.ui.playlist_detail

import com.ke.music.api.response.Playlist
import com.ke.music.api.response.PlaylistDynamicResponse
import com.ke.music.api.response.Song

sealed interface PlaylistDetailUiState {
    object Loading : PlaylistDetailUiState

    data class Detail(
        val shareCount: Int,
        val commentCount: Int,
        val playCount: Int,
        val bookedCount: Int,
        val songs: List<Song>,
        val playlist: Playlist,
        val subscribed: Boolean
    ) : PlaylistDetailUiState {
        companion object {
            fun from(
                playlist: Playlist,
                songs: List<Song>,
                dynamic: PlaylistDynamicResponse
            ): Detail {
                return Detail(
                    dynamic.shareCount,
                    dynamic.commentCount,
                    dynamic.playCount,
                    dynamic.bookedCount,
                    songs,
                    playlist,
                    dynamic.subscribed
                )
            }
        }
    }

    object Error : PlaylistDetailUiState
}