package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ke.music.room.db.entity.PlaylistTag
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTagDao {
    @Insert
    suspend fun insertAll(list: List<PlaylistTag>)

    @Query("select * from playlist_tag")
    fun getAll(): Flow<List<PlaylistTag>>


    @Query("select * from playlist_tag")
    suspend fun getAllTags(): List<PlaylistTag>

    @Update
    suspend fun update(playlistTag: PlaylistTag)
}