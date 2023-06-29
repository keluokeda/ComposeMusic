package com.ke.compose.music.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ke.compose.music.db.entity.AlbumArtistCrossRef

@Dao
interface AlbumArtistCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<AlbumArtistCrossRef>)
}