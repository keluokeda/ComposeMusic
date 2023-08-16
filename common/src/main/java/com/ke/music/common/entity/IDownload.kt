package com.ke.music.common.entity

interface IDownload {
    /**
     * 数据库主键
     */
    val id: Long

    /**
     * 资源id
     */
    val sourceId: Long

    /**
     * 下载状态
     */
    val downloadStatus: DownloadStatus

    /**
     * 下载器的id
     */
    val downloadId: Long?

    /**
     * 资源类型
     */
    val downloadSourceType: DownloadSourceType

    /**
     * 保存的路径
     */
    val path: String?

    /**
     * 创建时间
     */
    val createdTime: Long
}