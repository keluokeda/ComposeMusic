package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.music.room.db.entity.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(
        list: List<Playlist>,
    )

    /**
     * 插入之前先检查一下，防止没有分享、订阅评论数据的覆盖有数据的
     */
    suspend fun insertBeforeCheck(list: List<Playlist>) {
        insertAll(list.filter {
            val target = findPlaylistById(it.id)
            //如果是空 就写入 或者有信息就写入
            target == null || target.hasPayload
        })
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(playlist: Playlist)


    /**
     * 获取用户创建的歌单
     */
    @Query("select id,creator_id,cover_image_url,name,tags,description,track_count,play_count,update_time,share_count,booked_count,comment_count from playlist inner join user_playlist_cross_ref on playlist_id = playlist.id where creator_id = :userId order by `index`")
    fun getUserCreatedPlaylist(
        userId: Long,
    ): Flow<List<Playlist>>

    /**
     * 获取用户关注的歌单
     */
    @Query("select id,creator_id,cover_image_url,name,tags,description,track_count,play_count,update_time,share_count,comment_count,booked_count from playlist inner join playlist_subscriber_cross_ref on id = playlist_id where user_id = :userId order by `index`")
    fun getUserFollowingPlaylist(
        userId: Long,
    ): Flow<List<Playlist>>

    @Query("select * from playlist where id = :id")
    fun findById(id: Long): Flow<Playlist?>

    @Query("select * from playlist where id = :id")
    suspend fun findPlaylistById(id: Long): Playlist?

    /**
     * 根据id删除歌单
     */
    @Query("delete from playlist where id = :playlistId")
    suspend fun deleteByPlaylistId(playlistId: Long)
}