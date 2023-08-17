package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ke.music.room.db.entity.Artist
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {

    suspend fun insertAll(
        list: List<Artist>,
    ) {
        insert(list)
    }


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllInner(
        list: List<Artist>,
    )


    /**
     * 没有头像才做更新，有头像就不做更新
     */
    suspend fun insert(list: List<Artist>) {
        val result = list.filter {
            val target = queryByArtistId(it.artistId)
            target?.avatar == null
        }
        insertAllInner(result)
    }

    @Transaction
    suspend fun insertArtists(list: List<com.ke.music.api.response.Artist>) {
        insert(list.map {
            Artist(it.id, it.name, it.avatar)
        })
    }

    @Query("select * from artist where artist_id = :artistId")
    suspend fun queryByArtistId(artistId: Long): Artist?

    suspend fun insert(
        artist: Artist,
    ) {
        insert(listOf(artist))
    }

    @Query("select * from artist where artist_id = :artistId")
    fun getArtist(artistId: Long): Flow<Artist?>


}