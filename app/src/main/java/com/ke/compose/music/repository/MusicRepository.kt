package com.ke.compose.music.repository

import com.ke.compose.music.db.dao.AlbumDao
import com.ke.compose.music.db.dao.ArtistDao
import com.ke.compose.music.db.dao.MusicArtistCrossRefDao
import com.ke.compose.music.db.dao.MusicDao
import com.ke.compose.music.db.entity.Album
import com.ke.compose.music.db.entity.Artist
import com.ke.compose.music.db.entity.Download
import com.ke.compose.music.db.entity.Music
import com.ke.compose.music.db.entity.MusicArtistCrossRef
import com.ke.compose.music.entity.MusicEntity
import com.ke.compose.music.entity.QueryMusicResult
import com.ke.music.api.response.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicRepository @Inject constructor(
    private val musicDao: MusicDao,
    private val albumDao: AlbumDao,
    private val artistDao: ArtistDao,
    private val musicArtistCrossRefDao: MusicArtistCrossRefDao,
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

    fun getDownloadedMusics() = musicDao.getDownloadMusics(Download.STATUS_DOWNLOADED).map {
        queryResultToMusicEntityList(it)
    }


}