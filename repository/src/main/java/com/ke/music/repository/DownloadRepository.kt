package com.ke.music.repository

import com.ke.music.common.entity.DownloadSourceType
import com.ke.music.common.entity.DownloadStatus
import com.ke.music.common.entity.IDownload
import com.ke.music.common.entity.ISongEntity
import com.ke.music.common.repository.DownloadRepository
import com.ke.music.room.db.dao.DownloadDao
import com.ke.music.room.db.entity.Album
import com.ke.music.room.db.entity.Download
import com.ke.music.room.entity.MusicEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadRepository @Inject constructor(private val downloadDao: DownloadDao) :
    DownloadRepository {


    override suspend fun setDownloadIdle(list: List<Long>, downloadSourceType: DownloadSourceType) {
        download(list, downloadSourceType.type)
    }

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


    override fun getAll(downloadSourceType: DownloadSourceType): Flow<List<IDownload>> {
        return getAll(downloadSourceType.type)
    }

    /**
     * 获取所有
     */
    fun getAll(sourceType: Int) = downloadDao.getAll(sourceType)


    /**
     * 设置下载出错
     */
    override suspend fun setDownloadError(sourceId: Long) = downloadDao.setDownloadError(sourceId)

    override suspend fun setDownloadSuccess(
        sourceId: Long,
        path: String,
        downloadSourceType: DownloadSourceType,
    ) = downloadDao.setDownloadSuccess(sourceId, path, downloadSourceType.type)

    /**
     * 设置下载成功
     */
    suspend fun setDownloadSuccess(sourceId: Long, path: String, sourceType: Int) =
        downloadDao.setDownloadSuccess(sourceId, path, sourceType)


    override suspend fun updateStatus(sourceId: Long, downloadStatus: DownloadStatus) {
        updateStatus(sourceId, downloadStatus.status)
    }


    suspend fun updateStatus(sourceId: Long, status: Int) =
        downloadDao.updateStatus(sourceId, status)


    override suspend fun canDownload(sourceId: Long, downloadSourceType: DownloadSourceType) =
        canDownload(sourceId, downloadSourceType.type)

    /**
     * 是否可以下载
     */
    suspend fun canDownload(sourceId: Long, sourceType: Int): Boolean {
        return downloadDao.findBySourceTypeAndSourceId(sourceType, sourceId) == null
    }

    override suspend fun deleteDownloadedSong(songId: Long): Boolean = deleteDownloadedMusic(songId)

    suspend fun deleteDownloadedMusic(musicId: Long): Boolean {
        val download = downloadDao.findBySourceTypeAndSourceId(Download.SOURCE_TYPE_MUSIC, musicId)
            ?: return false

        val path = download.path ?: return false

        val file = File(path)
        file.delete()

        downloadDao.delete(download)

        return true
    }

    override fun getDownloadingSongs(): Flow<List<Pair<ISongEntity, Long>>> = getDownloadingMusics()
        .map { list ->

            list.map {
                MusicEntity(
                    it.musicId,
                    it.name,
                    0,
                    Album(it.albumId, it.albumName, it.albumImage),
                    emptyList(),
                    it.status
                ) to it.id
            }
        }

    fun getDownloadingMusics() = downloadDao.getDownloadingMusics()


    override suspend fun retryDownload(id: Long) {
        downloadDao.retry(id)
    }

    override suspend fun delete(id: Long) = downloadDao.delete(id)

    override suspend fun setDownloadId(
        sourceId: Long,
        downloadSourceType: DownloadSourceType,
        downloadId: Long,
    ) {
        downloadDao.setDownloadId(sourceId, downloadSourceType.type, downloadId)

    }

    suspend fun setDownloadId(sourceId: Long, sourceType: Int, downloadId: Long) {
        downloadDao.setDownloadId(sourceId, sourceType, downloadId)
    }
}