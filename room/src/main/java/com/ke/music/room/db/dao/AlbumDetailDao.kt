package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ke.music.room.db.entity.AlbumDetail

@Dao
interface AlbumDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(albumDetail: AlbumDetail)
}