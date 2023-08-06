package com.ke.music.room.entity

data class QueryMusicResult(
    val musicId: Long,
    val name: String,
    val mv: Long,
    val albumId: Long,
    val albumName: String,
    val albumImage: String,
    val artistId: Long,
    val artistName: String,
    val downloadStatus: Int? = null
)