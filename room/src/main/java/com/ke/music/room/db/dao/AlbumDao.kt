package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.music.room.db.entity.Album
import com.ke.music.room.entity.AlbumEntity

import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Album>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(album: Album)


    /**
     * 根据id查询专辑实体
     */
    @Query(
        "select  album.album_id as albumId,album.name as name ,album.image_url as image,album_detail.description ,album_detail.artist_id as artistId ,artist.name as artistName,user_album_cross_ref.`index`\n" +
                "from album \n" +
                "inner join album_detail on album.album_id = album_detail.album_id \n" +
                "inner join artist on artist.artist_id = album_detail.artist_id \n" +
                "left join user_album_cross_ref on user_album_cross_ref.album_id = album.album_id and user_album_cross_ref.user_id = :userId  " +
                "where album.album_id = :albumId"
    )
    fun findById(albumId: Long, userId: Long): Flow<AlbumEntity?>
}