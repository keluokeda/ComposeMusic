package com.ke.music.common.entity

/**
 * 歌曲
 */
interface ISong {
    val id: Long
    val name: String
    val albumId: Long
    val mv: Long
}