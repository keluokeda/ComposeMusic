package com.ke.music.room.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.music.room.db.entity.HotArtistCrossRef
import com.ke.music.room.entity.QueryHotArtistResult

@Dao
interface HotArtistCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<HotArtistCrossRef>)


    /**
     * 删除符合地区和类型的全部记录
     */
    @Query("delete from hot_artist_cross_ref where area = :area and type = :type")
    suspend fun deleteByAreaAndType(area: Int, type: Int)


    @Query("select hot_artist_cross_ref.id as id , artist.artist_id as artistId,name,avatar from hot_artist_cross_ref inner join artist on artist.artist_id = hot_artist_cross_ref.artist_id where area = :area and type = :type")
    fun getHotArtists(area: Int, type: Int): PagingSource<Int, QueryHotArtistResult>
}