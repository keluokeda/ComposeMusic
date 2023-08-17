package com.ke.music.common.repository

import com.ke.music.common.entity.DownloadSourceType
import com.ke.music.common.entity.DownloadStatus
import com.ke.music.common.entity.IDownload
import com.ke.music.common.entity.ISongEntity
import kotlinx.coroutines.flow.Flow

interface DownloadRepository {


    /**
     * 设置下载状态为空闲状态
     */
    suspend fun setDownloadIdle(list: List<Long>, downloadSourceType: DownloadSourceType)

    /**
     * 获取所有的下载任务
     */
    fun getAll(downloadSourceType: DownloadSourceType): Flow<List<IDownload>>
    suspend fun setDownloadError(sourceId: Long)

    /**
     * 设置下载成功
     */
    suspend fun setDownloadSuccess(
        sourceId: Long,
        path: String,
        downloadSourceType: DownloadSourceType,
    )

    /**
     * 更新下载状态
     */
    suspend fun updateStatus(sourceId: Long, downloadStatus: DownloadStatus)

    /**
     * 是否可以下载
     */
    suspend fun canDownload(sourceId: Long, downloadSourceType: DownloadSourceType): Boolean

    /**
     * 删除下载的歌曲
     */
    suspend fun deleteDownloadedSong(songId: Long): Boolean

    /**
     * 获取所有的下载中的歌曲
     */
    fun getDownloadingSongs(): Flow<List<Pair<ISongEntity, Long>>>

    /**
     * 下载重试
     */
    suspend fun retryDownload(id: Long)

    /**
     * 删除下载
     */
    suspend fun delete(id: Long)

    /**
     * 设置下载id
     */
    suspend fun setDownloadId(
        sourceId: Long,
        downloadSourceType: DownloadSourceType,
        downloadId: Long,
    )


    /**
     * 开始下载
     */
    @Deprecated("弃用")
    suspend fun download(list: List<Long>, sourceType: Int)

    suspend fun download(list: List<Long>, sourceType: DownloadSourceType)

}