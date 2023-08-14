package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ke.music.room.db.entity.SongLrc

@Dao
interface SongLrcDao {
    @Insert
    suspend fun insert(songLrc: SongLrc)

    @Query("select * from song_lrc where song_id = :songId")
    suspend fun findById(songId: Long): SongLrc?
}