package com.ke.music.repository

import android.content.Context
import com.ke.music.api.response.Song
import com.ke.music.room.db.dao.AlbumDao
import com.ke.music.room.db.dao.ArtistDao
import com.ke.music.room.db.dao.DownloadDao
import com.ke.music.room.db.dao.LocalPlaylistSongDao
import com.ke.music.room.db.dao.MusicArtistCrossRefDao
import com.ke.music.room.db.dao.MusicDao
import com.ke.music.room.db.dao.RecommendSongDao
import com.ke.music.room.db.dao.SongLrcDao
import com.ke.music.room.db.dao.SongPlayRecordDao
import com.ke.music.room.db.entity.Album
import com.ke.music.room.db.entity.Artist
import com.ke.music.room.db.entity.Download
import com.ke.music.room.db.entity.LocalPlaylistSong
import com.ke.music.room.db.entity.Music
import com.ke.music.room.db.entity.MusicArtistCrossRef
import com.ke.music.room.db.entity.SongLrc
import com.ke.music.room.db.entity.SongPlayRecord
import com.ke.music.room.entity.MusicEntity
import com.ke.music.room.entity.QueryMusicResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository @Inject constructor(
    private val musicDao: MusicDao,
    private val albumDao: AlbumDao,
    private val artistDao: ArtistDao,
    private val recommendSongDao: RecommendSongDao,
    private val musicArtistCrossRefDao: MusicArtistCrossRefDao,
    private val downloadDao: DownloadDao,
    private val localPlaylistSongDao: LocalPlaylistSongDao,
    private val songPlayRecordDao: SongPlayRecordDao,
    @ApplicationContext private val context: Context,
    private val songLrcDao: SongLrcDao,
) {

    suspend fun findById(musicId: Long): Music? = musicDao.findById(musicId)

    /**
     * 保存歌曲列表到数据库
     */
    suspend fun saveMusicListToRoom(songs: List<Song>) {

        val albumList = mutableListOf<Album>()

        val artistMusicCrossRefList = mutableListOf<MusicArtistCrossRef>()

        val artistList = mutableListOf<Artist>()

        val musicList = songs.mapIndexed { _, it ->

            albumList.add(
                Album(
                    it.album.id,
                    it.album.name,
                    it.album.imageUrl
                )
            )

            val list = it.singers.map { singer ->
                artistList.add(
                    Artist(singer.id, singer.name)
                )
                MusicArtistCrossRef(it.id, singer.id, 0)

            }

            artistMusicCrossRefList.addAll(list)


            Music(it.id, it.name, it.album.id, it.mv)
        }

        musicDao.insertAll(musicList)
        albumDao.insertAll(albumList)
        artistDao.insertAll(artistList)
        musicArtistCrossRefDao.insertAll(artistMusicCrossRefList)
    }


//    /**
//     * 根据歌单id查询歌曲
//     */
//    fun getMusicListByPlaylistId(playlistId: Long): Flow<List<Song>> {
//        return musicDao.findByPlaylistId(playlistId)
//            .map { list ->
//                list.groupBy {
//                    it.musicId
//                }.map { map ->
//                    val musicId = map.key
//                    val artistList = map.value.map {
//                        Singer(it.artistId, it.artistName)
//                    }.toList()
//                    val first = map.value.first()
//
//
//                    Song(
//                        musicId,
//                        name = first.name,
//                        album = com.ke.music.api.response.Album(
//                            first.albumId,
//                            first.albumName,
//                            first.albumImage
//                        ),
//                        singers = artistList,
//                        mv = first.mv
//
//                    )
//                }
//            }
//    }


    /**
     * 根据歌单id查询歌曲
     */
    fun queryMusicListByPlaylistId(playlistId: Long): Flow<List<MusicEntity>> {
        return musicDao.findMusicsByPlaylistId(playlistId)
            .map { list ->
                queryResultToMusicEntityList(list)
            }
    }

    /**
     * 根据专辑id查询歌曲
     */
    fun queryMusicListByAlbumId(albumId: Long): Flow<List<MusicEntity>> {
        return musicDao.findMusicsByAlbumId(albumId)
            .map {
                queryResultToMusicEntityList(it)
            }
    }

    private fun queryResultToMusicEntityList(list: List<QueryMusicResult>) =
        list.groupBy {
            it.musicId
        }.map { map ->
            val musicId = map.key
            val artistList = map.value.map {
                Artist(it.artistId, it.artistName)
            }.toList()
            val first = map.value.first()


            MusicEntity(
                musicId,
                name = first.name,
                album = Album(
                    first.albumId,
                    first.albumName,
                    first.albumImage
                ),
                artists = artistList,
                mv = first.mv,
                downloadStatus = first.downloadStatus

            )
        }


    suspend fun findDownloadedMusic(id: Long) = musicDao.findDownloadedMusicById(id)

    /**
     * 获取下载的音乐
     */
    fun getDownloadedMusics() = musicDao.getDownloadMusics(Download.STATUS_DOWNLOADED).map {
        queryResultToMusicEntityList(it)
    }

    suspend fun getDownloadedSong(songId: Long) = downloadDao.findBySongId(songId)


    /**
     * 当前用户每日推荐歌曲
     */
    fun getRecommendSongs() =

        context.userIdFlow.flatMapLatest { userId ->
            musicDao.findRecommendSongs(userId)
                .map {
                    queryResultToMusicEntityList(it)
                }
        }


    /**
     * 歌手热门歌曲
     */
    fun artistHotSongs(artistId: Long) = musicDao.getArtistHotSongs(artistId)
        .map {
            queryResultToMusicEntityList(it)
        }


    /**
     * 音乐被播放的时候调用下这个方法
     */
    suspend fun onSongPlayed(songId: Long) {
        songPlayRecordDao.insert(
            SongPlayRecord(
                songId = songId
            )
        )
    }

    /**
     * 插入一条歌曲到本地播放列表
     */
    suspend fun insertSongIntoLocalPlaylist(songId: Long) {
        localPlaylistSongDao.insert(LocalPlaylistSong(songId))
    }

    suspend fun insertSongsToLocalPlaylist(list: List<Long>) {
        localPlaylistSongDao.insertAll(
            list.map {
                LocalPlaylistSong(it)
            }
        )
    }

    suspend fun getLocalPlaylistSongs() = localPlaylistSongDao.getLocalPlaylistSongs()

    fun getLocalPlaylistSongList() = localPlaylistSongDao.getLocalPlaylistSongList()

    /**
     * 清空本地播放列表
     */
    suspend fun clearLocalPlaylist() {
        localPlaylistSongDao.clear()
    }

    suspend fun deleteLocalPlaylistSong(songId: Long) = localPlaylistSongDao.delete(songId)


    /**
     * 保存歌曲歌词
     */
    suspend fun saveSongLrc(songId: Long, lrc: String) {
        songLrcDao.insert(SongLrc(songId, lrc))
    }

    /**
     * 获取歌曲歌词
     */
    suspend fun getSongLrc(songId: Long) = songLrcDao.findById(songId)?.lrc
}