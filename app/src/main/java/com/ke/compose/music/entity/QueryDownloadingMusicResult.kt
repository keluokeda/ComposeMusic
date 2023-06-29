package com.ke.compose.music.entity

data class QueryDownloadingMusicResult(
    val id: Long,
    val musicId: Long,
    val name: String,
    val albumName: String,
    val albumImage: String,
    val status: Int
) {
    val displayName: String
        get() = "$name - $albumName"
}