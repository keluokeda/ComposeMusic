package com.ke.compose.music.db.entity

import androidx.room.Entity

/**
 * 音乐集合下载状态
 */
@Entity("music_collection_download_status", primaryKeys = ["id", "type"])
data class MusicCollectionDownloadStatus(
    val id: Long,
    val type: Int,
    val status: Int
) {
    companion object {
        const val TYPE_PLAYLIST = 0
        const val TYPE_ALBUM = 1
        const val STATUS_IDLE = 0
        const val STATUS_DOWNLOADING = 1
        const val STATUS_DOWNLOADED = 2
    }
}
