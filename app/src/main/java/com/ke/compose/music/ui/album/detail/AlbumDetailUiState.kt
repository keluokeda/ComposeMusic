package com.ke.compose.music.ui.album.detail

import com.ke.music.room.entity.AlbumEntity
import com.ke.music.room.entity.MusicEntity


internal data class AlbumDetailUiState(
    val albumEntity: AlbumEntity?,
    val musicList: List<MusicEntity>
) {
    val hasData: Boolean
        get() = albumEntity != null && musicList.isNotEmpty()
}