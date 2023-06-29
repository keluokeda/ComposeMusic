package com.ke.compose.music.service

import android.content.Intent
import android.os.Environment
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import com.arialyy.annotations.Download
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.task.DownloadTask
import com.ke.compose.music.domain.DownloadMusicListUseCase
import com.ke.compose.music.domain.GetAlbumSongsUseCase
import com.ke.compose.music.domain.GetMusicUrlUseCase
import com.ke.compose.music.domain.GetPlaylistSongsUseCase
import com.ke.compose.music.domain.successOr
import com.ke.compose.music.repository.DownloadRepository
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * 音乐下载服务
 */
@AndroidEntryPoint
class MusicDownloadService : LifecycleService() {


    @Inject
    lateinit var getMusicUrlUseCase: GetMusicUrlUseCase


    @Inject
    lateinit var getPlaylistSongsUseCase: GetPlaylistSongsUseCase

    @Inject
    lateinit var getAlbumSongsUseCase: GetAlbumSongsUseCase

    @Inject
    lateinit var downloadMusicListUseCase: DownloadMusicListUseCase

    @Inject
    lateinit var downloadRepository: DownloadRepository


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_DOWNLOAD_MUSIC -> {
//                downloadMusic(musicId)

                lifecycleScope.launch {
                    val musicId = intent.getLongExtra(EXTRA_ID, 0L)
                    if (downloadRepository.canDownload(
                            musicId,
                            com.ke.compose.music.db.entity.Download.SOURCE_TYPE_MUSIC
                        )
                    ) {
                        downloadMusicList(listOf(musicId))
                    } else {
                        Logger.d("id是 $musicId 的歌曲已下载或正在下载")
                    }
                }

            }

            ACTION_DOWNLOAD_PLAYLIST -> {
                val playlistId = intent.getLongExtra(EXTRA_ID, 0L)

                lifecycleScope.launch {
                    val songs = getPlaylistSongsUseCase(playlistId).successOr(emptyList())
                    val targetList = mutableListOf<Long>()

                    songs.forEach {
                        if (downloadRepository.canDownload(
                                it.id,
                                com.ke.compose.music.db.entity.Download.SOURCE_TYPE_MUSIC
                            )
                        ) {
                            targetList.add(it.id)
                        }
                    }

                    downloadMusicList(targetList)

                }
            }

            ACTION_DOWNLOAD_ALBUM -> {
                val albumId = intent.getLongExtra(EXTRA_ID, 0L)

                lifecycleScope.launch {
                    val songs = getAlbumSongsUseCase(albumId).successOr(emptyList())
                    val targetList = mutableListOf<Long>()

                    songs.forEach {
                        if (downloadRepository.canDownload(
                                it.id,
                                com.ke.compose.music.db.entity.Download.SOURCE_TYPE_MUSIC
                            )
                        ) {
                            targetList.add(it.id)
                        }
                    }

                    downloadMusicList(targetList)

                }
            }

            else -> {

            }
        }



        return super.onStartCommand(intent, flags, startId)
    }


    /**
     * 批量下载歌曲
     */
    private fun downloadMusicList(list: List<Long>) {
        lifecycleScope.launch {
            downloadMusicListUseCase(list)
        }
    }

    override fun onCreate() {
        super.onCreate()
        Aria.download(this).register()

        Aria.download(this)
            .resumeAllTask()


        lifecycleScope.launch {
            downloadRepository.getAll(com.ke.compose.music.db.entity.Download.SOURCE_TYPE_MUSIC)
                .collect { list ->

//                    Logger.d("下载中的音乐列表发生变化 $list")

                    //正在下载的任务
                    val downloadingMusic =

                        list.find { it.status == com.ke.compose.music.db.entity.Download.STATUS_DOWNLOADING }

                    if (downloadingMusic == null) {
                        Logger.d("没有正在下载的任务")
                        val target =
                            list.firstOrNull { it.status == com.ke.compose.music.db.entity.Download.STATUS_DOWNLOAD_IDLE }
                        if (target != null) {
                            Logger.d("找到了需要下载的任务 $target")
                            downloadMusic(target)
                        }
                    } else {
                        //有正在下载的任务

//                        val downloadId = downloadingMusic.downloadId
//
//                        if (downloadId != null) {
//                            val downloadTarget = Aria.download(this@MusicDownloadService)
//                                .load(downloadId)
//
//                            if (downloadTarget == null) {
//                                downloadRepository.setDownloadError(downloadingMusic.sourceId)
//                                return@collect
//                            }
//                            if (downloadTarget.isRunning) {
//                                Logger.d("下载任务 $downloadingMusic 正在运行")
//                                return@collect
//                            } else {
//                                Logger.d("下载任务 $downloadingMusic 已经停止")
//                                downloadTarget.resume()
//                            }
//                        } else {
//                            Logger.d("找不到下载任务 $downloadingMusic")
//                            downloadRepository.setDownloadError(downloadingMusic.sourceId)
//                        }


                    }

                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Aria.download(this).unRegister()
    }


    @Download.onTaskComplete
    fun onDownloadSuccess(task: DownloadTask) {


        lifecycleScope.launch {

            val musicId = task.extendField.toLong()
            val path = task.filePath


            Logger.d("onDownloadSuccess = $musicId  $path")

            downloadRepository.setDownloadSuccess(
                musicId,
                path,
                com.ke.compose.music.db.entity.Download.SOURCE_TYPE_MUSIC
            )
        }

    }

    @Download.onTaskFail
    fun onDownloadError(task: DownloadTask, exception: Exception?) {


        lifecycleScope.launch {


            val musicId = task.extendField.toLong()
            Logger.d("下载任务出错了 $musicId")
            exception?.printStackTrace()
            downloadRepository.setDownloadError(musicId)
        }
    }


    /**
     * 下载一个音乐
     */
    private fun downloadMusic(download: com.ke.compose.music.db.entity.Download) {

        if (download.status != com.ke.compose.music.db.entity.Download.STATUS_DOWNLOAD_IDLE) {
            Logger.d("要下载的任务状态不对 $download")
            return
        }

        val musicId = download.sourceId

        lifecycleScope.launch {


            val musicUrl = getMusicUrlUseCase(musicId).successOr("")

            val newStatus = if (musicUrl.isNotEmpty()) {
//                download.status = com.ke.compose.music.db.entity.Download.STATUS_DOWNLOADING
                val path = File(
                    getAppMusicDir(),
                    "$musicId" + getFileExtensionFromUrl(musicUrl)
                ).path
                val downloadId = Aria.download(this@MusicDownloadService)
                    .load(musicUrl)

                    .setFilePath(
                        path
                    )
                    .setExtendField("$musicId")
                    .ignoreCheckPermissions()
                    .ignoreFilePathOccupy()
                    .create()

                downloadRepository.setDownloadId(
                    musicId,
                    com.ke.compose.music.db.entity.Download.SOURCE_TYPE_MUSIC,
                    downloadId
                )
                com.ke.compose.music.db.entity.Download.STATUS_DOWNLOADING

            } else {

                //无法获取下载地址
//                download.status = com.ke.compose.music.db.entity.Download.STATUS_DOWNLOAD_ERROR
                com.ke.compose.music.db.entity.Download.STATUS_DOWNLOAD_ERROR
            }

//            downloadingDao.update(downloadingMusic)
            downloadRepository.updateStatus(musicId, newStatus)
        }
    }


    /**
     * 获取下载文件下
     */
    private fun getAppMusicDir() = getExternalFilesDir(Environment.DIRECTORY_MUSIC)


    /**
     * 根据文件地址获取文件拓展名 包含.
     */
    private fun getFileExtensionFromUrl(url: String): String {
        val index = url.lastIndexOf('.')
        return url.substring(index)
    }


    companion object {
        /**
         * 下载单个音乐
         */
        const val ACTION_DOWNLOAD_MUSIC = "ACTION_DOWNLOAD_MUSIC"

        /**
         * 下载整个歌单
         */
        const val ACTION_DOWNLOAD_PLAYLIST = "ACTION_DOWNLOAD_PLAYLIST"

        /**
         * 下载整个专辑
         */
        const val ACTION_DOWNLOAD_ALBUM = "ACTION_DOWNLOAD_ALBUM"
        const val EXTRA_ID = "EXTRA_ID"

    }
}