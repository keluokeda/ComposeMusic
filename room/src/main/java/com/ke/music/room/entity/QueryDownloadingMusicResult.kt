package com.ke.music.room.entity

data class QueryDownloadingMusicResult(
    val id: Long,
    val musicId: Long,
    val name: String,
    val albumId: Long,
    val albumName: String,
    val albumImage: String,
    val status: Int,
) {
    val displayName: String
        get() = "$name - $albumName"
}