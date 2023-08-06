package com.ke.music.room.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.music.room.db.entity.Playlist
import com.ke.music.room.db.entity.TopPlaylist

@Dao
interface TopPlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        list: List<TopPlaylist>
    )

    /**
     * 根据分类查询歌单 distinct防止重复结果
     */
    @Query("select distinct playlist.* from top_playlist inner join playlist on top_playlist.playlist_id = playlist.id where category = :category")
    fun queryByCategory(category: String?): PagingSource<Int, Playlist>

    /**
     * 删除
     */
    @Query("delete from top_playlist where category = :category")
    suspend fun deleteByCategory(category: String?)
}