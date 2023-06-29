package com.ke.compose.music.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ke.compose.music.db.entity.MusicArtistCrossRef

@Dao
interface MusicArtistCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<MusicArtistCrossRef>)
}