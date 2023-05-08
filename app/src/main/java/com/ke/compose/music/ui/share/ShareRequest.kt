package com.ke.compose.music.ui.share

data class ShareRequest(
    val shareType: ShareType,
    val id: Long,
    val message: String,
    val users: List<Long>
)

/**
 * 分享的类型
 */
enum class ShareType {
    Playlist,
    Album,
    Song
}