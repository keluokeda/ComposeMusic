package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import com.ke.music.room.db.entity.SongPlayRecord

@Dao
interface SongPlayRecordDao {


    @Insert
    suspend fun insert(
        record: SongPlayRecord
    )


}