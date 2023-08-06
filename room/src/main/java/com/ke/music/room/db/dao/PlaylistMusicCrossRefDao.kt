package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.music.room.db.entity.PlaylistMusicCrossRef

@Dao
interface PlaylistMusicCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        list: List<PlaylistMusicCrossRef>
    )

    @Query("delete from playlist_music_cross_ref where playlist_id = :playlistId and music_id = :musicId")
    suspend fun deleteByPlaylistIdAndMusicId(playlistId: Long, musicId: Long)
}