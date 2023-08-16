package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ke.music.common.entity.DownloadSourceType
import com.ke.music.common.entity.DownloadStatus
import com.ke.music.common.entity.IDownload

@Entity(tableName = "download")
data class Download(
    /**
     * 主键
     */
    @PrimaryKey(autoGenerate = true)
    override val id: Long = 0,

    /**
     * 资源id
     */
    @ColumnInfo("source_id")
    override val sourceId: Long,


    /**
     * 下载器返回的id
     */
    @ColumnInfo(name = "download_id")
    override val downloadId: Long? = null,

    /**
     * 资源类型
     */
    @ColumnInfo("source_type")
    val sourceType: Int,

    var status: Int,

    override var path: String?,

    /**
     * 创建时间
     */
    @ColumnInfo(name = "created_time")
    override val createdTime: Long = System.currentTimeMillis(),
) : IDownload {
    companion object {
        /**
         * 未下载
         */
        const val STATUS_DOWNLOAD_IDLE = -1

        /**
         * 下载出错
         */
        const val STATUS_DOWNLOAD_ERROR = -2

        /**
         * 下载中
         */
        const val STATUS_DOWNLOADING = -3

        /**
         * 已下载
         */
        const val STATUS_DOWNLOADED = -4


        const val SOURCE_TYPE_MUSIC = 0
    }

    override val downloadStatus: DownloadStatus
        get() = when (status) {
            Download.STATUS_DOWNLOADED -> DownloadStatus.Downloaded
            Download.STATUS_DOWNLOADING -> DownloadStatus.Downloading
            Download.STATUS_DOWNLOAD_ERROR -> DownloadStatus.Error
            STATUS_DOWNLOAD_IDLE -> DownloadStatus.Idle
            else -> throw IllegalArgumentException("错误的status $status")

        }
    override val downloadSourceType: DownloadSourceType
        get() = when (sourceType) {
            SOURCE_TYPE_MUSIC -> DownloadSourceType.Song
            else -> DownloadSourceType.Mv
        }
}
