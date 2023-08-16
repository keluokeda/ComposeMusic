package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.music.room.db.entity.PlaylistSubscriberCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistSubscriberCrossRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<PlaylistSubscriberCrossRef>)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlistSubscriberCrossRef: PlaylistSubscriberCrossRef)

    /**
     * 删除歌单和用户的订阅关系
     */
    @Query("delete from playlist_subscriber_cross_ref where playlist_id = :playlistId and user_id = :userId")
    suspend fun deleteByPlaylistIdAndUserId(playlistId: Long, userId: Long)


    /**
     * 判断用户有没有订阅歌单
     */
    @Query("select * from playlist_subscriber_cross_ref where user_id = :userId and playlist_id = :playlistId")
    fun findByUserIdAndPlaylistId(userId: Long, playlistId: Long): Flow<PlaylistSubscriberCrossRef?>

    @Query("select * from playlist_subscriber_cross_ref where user_id = :userId and playlist_id = :playlistId")
    suspend fun getByUserIdAndPlaylistId(
        userId: Long,
        playlistId: Long,
    ): PlaylistSubscriberCrossRef?
}