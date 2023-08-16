package com.ke.music.common.entity

/**
 * 下载状态
 */
enum class DownloadStatus(val status: Int) {
    Downloading(-3), Error(-2), Idle(-1), Downloaded(-4)
}