package com.ke.compose.music.entity

import com.ke.compose.music.db.entity.Album
import com.ke.compose.music.db.entity.Artist

data class MusicEntity(
    val musicId: Long,
    val name: String,
    val mv: Long,
    val album: Album,
    val artists: List<Artist>,
    /**
     * 下载状态
     */
    val downloadStatus: Int?
)
