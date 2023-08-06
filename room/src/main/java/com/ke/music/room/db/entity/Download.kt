package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "download")
data class Download(
    /**
     * 主键
     */
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /**
     * 资源id
     */
    @ColumnInfo("source_id")
    val sourceId: Long,


    /**
     * 下载器返回的id
     */
    @ColumnInfo(name = "download_id")
    val downloadId: Long? = null,

    /**
     * 资源类型
     */
    @ColumnInfo("source_type")
    val sourceType: Int,

    var status: Int,

    var path: String?,

    /**
     * 创建时间
     */
    @ColumnInfo(name = "created_time")
    val createdTime: Long = System.currentTimeMillis(),
) {
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
}
