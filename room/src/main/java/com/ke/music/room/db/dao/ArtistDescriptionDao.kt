package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.ke.music.room.db.entity.ArtistDescription
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDescriptionDao {
    @Insert
    suspend fun insert(list: List<ArtistDescription>)

    @Query("delete from artist_description where artist_id = :artistId")
    suspend fun deleteByArtistId(artistId: Long)


    /**
     * 根据id查询结果
     */
    @Query("select * from artist_description where artist_id = :artistId")
    fun getListByArtistId(artistId: Long): Flow<List<ArtistDescription>>

    /**
     * 重置歌手信息
     */
    @Transaction
    suspend fun resetArtistDescription(
        id: Long, list: List<ArtistDescription>
    ) {
        deleteByArtistId(id)
        insert(list)
    }
}