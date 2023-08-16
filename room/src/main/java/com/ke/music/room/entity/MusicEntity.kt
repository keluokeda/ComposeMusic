package com.ke.music.room.entity

import com.ke.music.common.entity.DownloadStatus
import com.ke.music.common.entity.ISong
import com.ke.music.common.entity.ISongEntity
import com.ke.music.room.db.entity.Album
import com.ke.music.room.db.entity.Artist
import com.ke.music.room.db.entity.Download
import com.ke.music.room.db.entity.Music


data class MusicEntity(
    val musicId: Long,
    val name: String,
    val mv: Long,
    override val album: Album,
    override val artists: List<Artist>,
    /**
     * 下载状态
     */
    val downloadStatus: Int?,

    ) : ISongEntity {
    val subTitle: String
        get() = "${artists.joinToString("/") { it.name }} ${album.name}"


    override val song: ISong
        get() = Music(musicId, name, album.albumId, mv)

    override val status: DownloadStatus
        get() = when (downloadStatus) {
            Download.STATUS_DOWNLOADING -> DownloadStatus.Downloading
            Download.STATUS_DOWNLOADED -> DownloadStatus.Downloaded
            Download.STATUS_DOWNLOAD_ERROR -> DownloadStatus.Error
            else -> {
                DownloadStatus.Idle
            }
        }

    override val path: String?
        get() = null
}
