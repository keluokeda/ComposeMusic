package com.ke.compose.music.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.compose.music.db.entity.Download
import com.ke.compose.music.entity.QueryDownloadingMusicResult
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(download: Download)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Download>)


    /**
     * 查询所有
     */
    @Query("select * from download where source_type = :sourceType")
    fun getAll(sourceType: Int): Flow<List<Download>>

    /**
     * 设置下载出错
     */
    @Query("update download set status = ${Download.STATUS_DOWNLOAD_ERROR} where source_id = :sourceId")
    suspend fun setDownloadError(sourceId: Long)

    /**
     * 设置下载成功
     */
    @Query(
        "update download set status = ${Download.STATUS_DOWNLOADED} , path = :path ,created_time = :createdTime where source_id = :sourceId and source_type = :sourceType"
    )
    suspend fun setDownloadSuccess(
        sourceId: Long,
        path: String,
        sourceType: Int,
        createdTime: Long = System.currentTimeMillis()
    )


    /**
     * 更新下载状态
     */
    @Query("update download  set status = :status where source_id = :sourceId")
    suspend fun updateStatus(sourceId: Long, status: Int)

//    @Query("select * from download where source_id = :sourceId and source_type = :sourceType ")
//    suspend fun queryBySourceId(sourceId: Long, sourceType: Int): Download?


    @Query("select * from download where source_id = :sourceId and source_type = :sourceType")
    suspend fun findBySourceTypeAndSourceId(sourceType: Int, sourceId: Long): Download?

    @Delete
    suspend fun delete(download: Download)

    @Query("delete from download where id = :id")
    suspend fun delete(id: Long)

    /**
     * 查询下载中的音乐
     */
    @Query(
        "select download.id,music.music_id as musicId,music.name ,album.name as albumName,album.image_url as albumImage,download.status \n" +
                "from download \n" +
                "inner join music on download.source_id = music.music_id \n" +
                "inner join album on music.album_id = album.album_id \n" +
                "where download.source_type = ${Download.SOURCE_TYPE_MUSIC} and download.status != ${Download.STATUS_DOWNLOADED}"
    )
    fun getDownloadingMusics(): Flow<List<QueryDownloadingMusicResult>>

    /**
     * 重试下载任务
     * 只有失败的才能重试
     */
    @Query("update download set status = ${Download.STATUS_DOWNLOAD_IDLE} where id = :id and status = ${Download.STATUS_DOWNLOAD_ERROR} ")
    suspend fun retry(id: Long)

    @Query("update download set download_id = :downloadId where source_id = :sourceId and source_type = :sourceType")
    suspend fun setDownloadId(sourceId: Long, sourceType: Int, downloadId: Long)
}