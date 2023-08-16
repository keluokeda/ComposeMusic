package com.ke.music.room.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ke.music.room.db.entity.NewAlbumCrossRef
import com.ke.music.room.entity.QueryNewAlbumResult

@Dao
interface NewAlbumCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<NewAlbumCrossRef>)

    @Query("delete from new_album_cross_ref where area = :area")
    suspend fun deleteByArea(area: String)

    @Transaction
    suspend fun resetNewAlbums(area: String, list: List<NewAlbumCrossRef>) {
        deleteByArea(area)
        insertAll(list)
    }

    @Query("select new_album_cross_ref.id ,new_album_cross_ref.album_id as albumId,album.name,album.image_url as image from album inner join new_album_cross_ref on album.album_id = new_album_cross_ref.album_id where area = :area")
    fun getNewAlbums(area: String): PagingSource<Int, QueryNewAlbumResult>
}