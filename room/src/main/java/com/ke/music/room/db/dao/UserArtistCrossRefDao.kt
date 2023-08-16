package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.music.room.db.entity.UserArtistCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface UserArtistCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userArtistCrossRef: UserArtistCrossRef)

    @Delete
    suspend fun delete(userArtistCrossRef: UserArtistCrossRef)


    /**
     * 判断用户是否关注了歌手
     */
    @Query("select * from user_artist_cross_ref where user_id = :userId and artist_id = :artistId")
    fun isUserFollowArtist(userId: Long, artistId: Long): Flow<UserArtistCrossRef?>
}