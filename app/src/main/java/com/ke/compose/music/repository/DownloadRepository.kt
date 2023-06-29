package com.ke.compose.music.repository

import com.ke.compose.music.db.dao.DownloadDao
import com.ke.compose.music.db.entity.Download
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepository @Inject constructor(private val downloadDao: DownloadDao) {


    /**
     * 开始下载
     */
    suspend fun download(list: List<Long>, sourceType: Int) {
        downloadDao.insertAll(
            list.map {
                Download(
                    sourceId = it,
                    sourceType = sourceType,
                    path = null,
                    status = Download.STATUS_DOWNLOAD_IDLE
                )
            }
        )
    }

    /**
     * 获取所有
     */
    fun getAll(sourceType: Int) = downloadDao.getAll(sourceType)


    /**
     * 设置下载出错
     */
    suspend fun setDownloadError(sourceId: Long) = downloadDao.setDownloadError(sourceId)

    /**
     * 设置下载成功
     */
    suspend fun setDownloadSuccess(sourceId: Long, path: String, sourceType: Int) =
        downloadDao.setDownloadSuccess(sourceId, path, sourceType)


    suspend fun updateStatus(sourceId: Long, status: Int) =
        downloadDao.updateStatus(sourceId, status)


    /**
     * 是否可以下载
     */
    suspend fun canDownload(sourceId: Long, sourceType: Int): Boolean {
        return downloadDao.findBySourceTypeAndSourceId(sourceType, sourceId) == null
    }

    suspend fun deleteDownloadedMusic(musicId: Long): Boolean {
        val download = downloadDao.findBySourceTypeAndSourceId(Download.SOURCE_TYPE_MUSIC, musicId)
            ?: return false

        val path = download.path ?: return false

        val file = File(path)
        file.delete()

        downloadDao.delete(download)

        return true
    }

    fun getDownloadingMusics() = downloadDao.getDownloadingMusics()

    suspend fun retryDownload(id: Long) {
        downloadDao.retry(id)
    }

    suspend fun delete(id: Long) = downloadDao.delete(id)
    suspend fun setDownloadId(sourceId: Long, sourceType: Int, downloadId: Long) {
        downloadDao.setDownloadId(sourceId, sourceType, downloadId)
    }
}