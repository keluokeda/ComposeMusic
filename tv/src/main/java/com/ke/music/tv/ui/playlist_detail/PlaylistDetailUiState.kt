package com.ke.music.tv.ui.playlist_detail

import com.ke.music.room.db.entity.Playlist
import com.ke.music.room.db.entity.User
import com.ke.music.room.entity.MusicEntity


data class PlaylistDetailUiState(
    val playlist: Playlist?,
    val songs: List<MusicEntity>,
    val subscribed: Boolean,
    val creator: User?
) {
    val hasData: Boolean
        get() = playlist != null && creator != null
}


