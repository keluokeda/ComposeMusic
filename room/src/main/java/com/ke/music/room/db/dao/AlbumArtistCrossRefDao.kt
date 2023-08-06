package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ke.music.room.db.entity.AlbumArtistCrossRef

@Dao
interface AlbumArtistCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<AlbumArtistCrossRef>)
}