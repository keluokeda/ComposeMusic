package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ke.music.room.db.entity.AllMv

@Dao
interface AllMvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<AllMv>)


    @Query("delete from all_mv where area = :area and type = :type")
    suspend fun deleteByAreaAndType(area: String, type: String)


    @Transaction
    suspend fun deleteOldAndInsertNew(area: String, type: String, list: List<AllMv>) {
        deleteByAreaAndType(area, type)
        insertAll(list)
    }
}