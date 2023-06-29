package com.ke.compose.music.entity

data class QueryDownloadedMusicResult(
    val musicId: Long,
    val name: String,
    val albumName: String,
    val albumImage: String,
    val path: String?
)