package com.ke.music.room.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.music.room.db.entity.HotArtist

@Dao
interface HotArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<HotArtist>)

    @Query("delete from hot_artist")
    suspend fun deleteAll()

    @Query("select * from hot_artist")
    fun getAll(): PagingSource<Int, HotArtist>
}