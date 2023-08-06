package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.compose.music.entity.QueryDownloadedMusicResult
import com.ke.music.room.db.entity.Download
import com.ke.music.room.db.entity.Music
import com.ke.music.room.entity.QueryMusicResult
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Music>)

    /**
     * 查询指定id的歌曲是否存在
     */
    @Query("select * from music where music_id = :id")
    suspend fun findById(
        id: Long
    ): Music?


//    @Query(
//        "select music.music_id as musicId,music.name,music.mv,album.album_id as albumId, album.name as albumName,album.image_url as albumImage,artist.artist_id artistId,artist.name as artistName from music \n" +
//                " inner join album on music.album_id = album.album_id \n" +
//                " inner join music_artist_cross_ref on music.music_id = music_artist_cross_ref.music_id  \n" +
//                " inner join artist on artist.artist_id = music_artist_cross_ref.artist_id\n" +
//                " inner join playlist_music_cross_ref on music.music_id = playlist_music_cross_ref.music_id where playlist_id = :playlistId order by playlist_music_cross_ref.`index`"
//    )
//    fun findByPlaylistId(playlistId: Long): Flow<List<QueryMusicByPlaylistIdResult>>


    @Query(
        "select music.music_id as musicId,\n" +
                "music.name as name,\n" +
                "music.mv,\n" +
                "album.album_id as albumId,\n" +
                "album.name as albumName,\n" +
                "album.image_url as albumImage ,\n" +
                "artist.artist_id as artistId,\n" +
                "artist.name as artistName,\n" +
                "download.status as downloadStatus\n" +
                "from music \n" +
                "inner join playlist_music_cross_ref on music.music_id = playlist_music_cross_ref.music_id \n" +
                "inner join album on music.album_id = album.album_id\n" +
                "inner join music_artist_cross_ref on music_artist_cross_ref.music_id = music.music_id\n" +
                "inner join artist on artist.artist_id = music_artist_cross_ref.artist_id\n" +
                "left join download on download.source_id = music.music_id and download.source_type = 0\n" +
                "\n" +
                "where playlist_id = :playlistId\n order by playlist_music_cross_ref.`index`"
    )
    fun findMusicsByPlaylistId(playlistId: Long): Flow<List<QueryMusicResult>>


    /**
     * 查询一个专辑下的所有歌曲
     */
    @Query(
        "select music.music_id as musicId,\n" +
                "music.name as name,\n" +
                "music.mv,\n" +
                "album.album_id as albumId,\n" +
                "album.name as albumName,\n" +
                "album.image_url as albumImage ,\n" +
                "artist.artist_id as artistId,\n" +
                "artist.name as artistName,\n" +
                "download.status as downloadStatus\n" +
                "from music \n" +
                "inner join album on music.album_id = album.album_id\n" +
                "inner join music_artist_cross_ref on music_artist_cross_ref.music_id = music.music_id\n" +
                "inner join artist on artist.artist_id = music_artist_cross_ref.artist_id\n" +
                "left join download on download.source_id = music.music_id and download.source_type = 0\n" +
                "\n" +
                "where albumId = :albumId\n"
    )
    fun findMusicsByAlbumId(albumId: Long): Flow<List<QueryMusicResult>>


    /**
     * 查询某个用户每日推荐歌曲
     */
    @Query(
        "select music.music_id as musicId,\n" +
                "music.name as name,\n" +
                "music.mv,\n" +
                "album.album_id as albumId,\n" +
                "album.name as albumName,\n" +
                "album.image_url as albumImage ,\n" +
                "artist.artist_id as artistId,\n" +
                "artist.name as artistName,\n" +
                "download.status as downloadStatus\n" +
                "from music \n" +
                "inner join album on music.album_id = album.album_id\n" +
                "inner join recommend_song on music.music_id = recommend_song.song_id\n" +
                "inner join music_artist_cross_ref on music_artist_cross_ref.music_id = music.music_id\n" +
                "inner join artist on artist.artist_id = music_artist_cross_ref.artist_id\n" +
                "left join download on download.source_id = music.music_id and download.source_type = 0\n" +
                "\n" +
                "where recommend_song.user_id = :userId\n"
    )
    fun findRecommendSongs(userId: Long): Flow<List<QueryMusicResult>>

    @Query(
        "select \n" +
                "music.music_id as musicId,\n" +
                "music.name as name,\n" +
                "album.name as albumName,\n" +
                "album.image_url as albumImage,\n" +
                "download.path as path\n" +
                "from music \n" +
                "inner join album on music.album_id = album.album_id \n" +
                "inner join download on download.source_id = music.music_id\n" +
                "where music_id = :id and download.source_type = 0 and download.status = -4"
    )
    suspend fun findDownloadedMusicById(id: Long): QueryDownloadedMusicResult?


    /**
     * 获取下载的歌曲
     */
    @Query(
        "select music.music_id as musicId,\n" +
                "music.name as name,\n" +
                "music.mv,\n" +
                "album.album_id as albumId,\n" +
                "album.name as albumName,\n" +
                "album.image_url as albumImage ,\n" +
                "artist.artist_id as artistId,\n" +
                "artist.name as artistName,\n" +
                "download.status as downloadStatus\n" +
                "from music \n" +
                "inner join album on music.album_id = album.album_id\n" +
                "inner join music_artist_cross_ref on music_artist_cross_ref.music_id = music.music_id\n" +
                "inner join artist on artist.artist_id = music_artist_cross_ref.artist_id\n" +
                "inner join download on download.source_id = music.music_id \n" +
                "where  download.status = :status and download.source_type = ${Download.SOURCE_TYPE_MUSIC} order by created_time desc"
    )
    fun getDownloadMusics(status: Int): Flow<List<QueryMusicResult>>
}