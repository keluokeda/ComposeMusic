package com.ke.compose.music.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ke.compose.music.db.entity.RecommendSong

@Dao
interface RecommendSongDao {
    @Insert
    suspend fun insertAll(list: List<RecommendSong>)


    /**
     * 清空某个用户的每日推荐
     */
    @Query("delete from recommend_song where user_id = :userId")
    suspend fun clearAll(userId: Long)
}