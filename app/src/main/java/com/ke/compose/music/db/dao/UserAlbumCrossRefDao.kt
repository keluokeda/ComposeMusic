package com.ke.compose.music.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.compose.music.db.entity.UserAlbumCrossRef

@Dao
interface UserAlbumCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userAlbumCrossRef: UserAlbumCrossRef)

    @Query("delete from user_album_cross_ref where user_id = :userId and album_id = :albumId")
    suspend fun deleteByUserIdAndAlbumId(userId: Long, albumId: Long)
}