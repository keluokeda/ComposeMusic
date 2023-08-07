package com.ke.music.room.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ke.music.room.db.entity.NewAlbum

@Dao
interface NewAlbumDao {

    @Insert
    suspend fun insertAll(list: List<NewAlbum>)

    @Query("select * from new_album where area = :area")
    fun getAlbumListByArea(
        area: String
    ): PagingSource<Int, NewAlbum>

    @Query("delete from new_album where area = :area")
    suspend fun deleteAllByArea(area: String)
}