package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.ke.music.room.db.entity.ArtistMusicCrossRef

@Dao
interface ArtistMusicCrossRefDao {
    @Insert
    suspend fun insertAll(list: List<ArtistMusicCrossRef>)

    @Query("delete from artist_music_cross_ref where artist_id = :artistId")
    suspend fun clearByArtistId(artistId: Long)

    @Transaction
    suspend fun resetArtistHotSongs(artistId: Long, list: List<ArtistMusicCrossRef>) {
        clearByArtistId(artistId)
        insertAll(list)
    }
}