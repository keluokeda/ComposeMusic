package com.ke.music.common.entity

data class AddOrRemoveSongsToPlaylistRequest(
    val playlistId: Long,
    val songIds: List<Long>,
    val add: Boolean,
)