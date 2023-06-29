package com.ke.compose.music.entity

data class DownloadedMusicEntity(
    val musicId: Long,
    val name: String,
    val path: String,
    val createdTime: Long,
    val mv: Long,
    val albumName: String,
    val albumImage: String
)
