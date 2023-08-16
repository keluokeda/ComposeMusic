package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.music.room.db.entity.Download
import com.ke.music.room.db.entity.LocalPlaylistSong
import com.ke.music.room.entity.DownloadedMusicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalPlaylistSongDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(localPlaylistSong: LocalPlaylistSong)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<LocalPlaylistSong>)


    @Query("delete from local_playlist_song")
    suspend fun clear()


//    @Query(
//        "select music.music_id as musicId,music.name as name,album_id as albumId,download.path as path,download.created_time as createdTime,music.mv as mv,album.name as albumName,album.image_url as albumImage\n" +
//                "from local_playlist_song inner join music on local_playlist_song.song_id = music.music_id\n" +
//                "inner join album on album.album_id = music.album_id\n" +
//                "inner join download on download.source_type = ${Download.SOURCE_TYPE_MUSIC} and download.source_id = music.music_id\n" +
//                "order by added_time asc"
//    )
//    suspend fun getLocalPlaylistSongs(): List<DownloadedMusicEntity>

    @Query(
        "select music.music_id as musicId,music.name as name,music.album_id as albumId,download.path as path,download.created_time as createdTime,music.mv as mv,album.name as albumName,album.image_url as albumImage\n" +
                "from local_playlist_song inner join music on local_playlist_song.song_id = music.music_id\n" +
                "inner join album on album.album_id = music.album_id\n" +
                "inner join download on download.source_type = ${Download.SOURCE_TYPE_MUSIC} and download.source_id = music.music_id\n" +
                "order by added_time asc"
    )
    fun getLocalPlaylistSongList(): Flow<List<DownloadedMusicEntity>>


    /**
     * 删除单个
     */
    @Query("delete from local_playlist_song where song_id = :songId")
    suspend fun delete(songId: Long)
}