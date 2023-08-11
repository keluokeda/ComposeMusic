package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ke.music.room.db.entity.MvArtistCrossRef

@Dao
interface MvArtistCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<MvArtistCrossRef>)

    @Query("delete from mv_artist_cross_ref where artist_id = :artistId")
    suspend fun deleteAllByArtistId(artistId: Long)

    @Transaction
    suspend fun resetArtistMv(artistId: Long, list: List<MvArtistCrossRef>) {
        deleteAllByArtistId(artistId)
        insertAll(list)
    }
}