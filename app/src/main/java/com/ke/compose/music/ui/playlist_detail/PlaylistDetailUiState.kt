package com.ke.compose.music.ui.playlist_detail

import com.ke.compose.music.db.entity.Playlist
import com.ke.compose.music.db.entity.User
import com.ke.compose.music.entity.MusicEntity


data class PlaylistDetailUiState(
    val playlist: Playlist?,
    val songs: List<MusicEntity>,
    val subscribed: Boolean,
    val creator: User?
) {
    val hasData: Boolean
        get() = playlist != null && creator != null
}


