package com.ke.compose.music.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ke.compose.music.db.entity.Artist

@Dao
interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        list: List<Artist>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(
        artist: Artist
    )

}