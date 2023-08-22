package com.ke.music.download

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.staticCompositionLocalOf

interface DownloadManager {
    /**
     * 下载音乐
     */
    fun downloadSong(songId: Long)


    /**
     * 下载歌单的全部歌曲
     */
    fun downloadPlaylist(playlistId: Long)

    /**
     * 下载专辑
     */
    fun downloadAlbum(albumId: Long)

    /**
     * 下载每日推荐的音乐
     */
    fun downloadRecommendSongs()

    /**
     * 重试
     */
    fun retry(songId: Long)
}


class DownloadManagerImpl(context: Context) : DownloadManager {
    private val applicationContext = context.applicationContext
    override fun downloadSong(songId: Long) {
        val intent = Intent(applicationContext, MusicDownloadService::class.java)
        intent.action = MusicDownloadService.ACTION_DOWNLOAD_SONG
        intent.putExtra(MusicDownloadService.EXTRA_ID, songId)
        applicationContext.startService(intent)
    }

    override fun downloadPlaylist(playlistId: Long) {
        val intent = Intent(applicationContext, MusicDownloadService::class.java)
        intent.action = MusicDownloadService.ACTION_DOWNLOAD_PLAYLIST
        intent.putExtra(MusicDownloadService.EXTRA_ID, playlistId)
        applicationContext.startService(intent)
    }


    override fun downloadAlbum(albumId: Long) {
        val intent = Intent(applicationContext, MusicDownloadService::class.java)
        intent.action = MusicDownloadService.ACTION_DOWNLOAD_ALBUM
        intent.putExtra(MusicDownloadService.EXTRA_ID, albumId)
        applicationContext.startService(intent)
    }

    override fun downloadRecommendSongs() {
        val intent = Intent(applicationContext, MusicDownloadService::class.java)
        intent.action = MusicDownloadService.ACTION_DOWNLOAD_RECOMMEND_SONG
        applicationContext.startService(intent)
    }

    override fun retry(songId: Long) {
        val intent = Intent(applicationContext, MusicDownloadService::class.java)
        intent.action = MusicDownloadService.ACTION_DOWNLOAD_RETRY
        intent.putExtra(MusicDownloadService.EXTRA_ID, songId)
        applicationContext.startService(intent)
    }

}

val LocalDownloadManager = staticCompositionLocalOf<DownloadManager> {
    object : DownloadManager {
        override fun downloadSong(songId: Long) {

        }

        override fun downloadPlaylist(playlistId: Long) {
        }

        override fun downloadAlbum(albumId: Long) {
        }

        override fun downloadRecommendSongs() {
        }

        override fun retry(songId: Long) {

        }

    }
}

