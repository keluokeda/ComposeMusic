package com.ke.compose.music.repository

import com.ke.compose.music.db.dao.AlbumArtistCrossRefDao
import com.ke.compose.music.db.dao.AlbumDao
import com.ke.compose.music.db.dao.AlbumDetailDao
import com.ke.compose.music.db.dao.ArtistDao
import com.ke.compose.music.db.dao.UserAlbumCrossRefDao
import com.ke.compose.music.db.entity.Album
import com.ke.compose.music.db.entity.AlbumArtistCrossRef
import com.ke.compose.music.db.entity.AlbumDetail
import com.ke.compose.music.db.entity.Artist
import com.ke.compose.music.db.entity.UserAlbumCrossRef
import com.ke.music.api.response.AlbumResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlbumRepository @Inject constructor(
    private val albumDao: AlbumDao,
    private val albumDetailDao: AlbumDetailDao,
    private val albumArtistCrossRefDao: AlbumArtistCrossRefDao,
    private val userAlbumCrossRefDao: UserAlbumCrossRefDao,
    private val artistDao: ArtistDao,
    private val musicRepository: MusicRepository,
    private val userIdRepository: UserIdRepository
) {

    fun getAlbumEntity(albumId: Long) = albumDao.findById(albumId, userIdRepository.userId)

    /**
     * 保存接口数据到room
     * @param collected 当前用户是否收藏了专辑
     */
    suspend fun saveAlbumResponseToRoom(albumResponse: AlbumResponse, collected: Boolean) {
        musicRepository.saveMusicListToRoom(albumResponse.songs)

        val album =
            Album(albumResponse.album.id, albumResponse.album.name, albumResponse.album.picUrl)
        val albumDetail = AlbumDetail(
            albumResponse.album.id,
            albumResponse.album.publishTime,
            albumResponse.album.company,
            albumResponse.album.description,
            albumResponse.album.artist.id
        )

        albumDao.insert(album)
        albumDetailDao.insert(albumDetail)

        //写入歌手列表
        artistDao.insertAll(
            albumResponse.album.artists.map {
                Artist(it.id, it.name)
            }
        )

        //写入歌手
        artistDao.insert(
            Artist(
                albumResponse.album.artist.id, albumResponse.album.artist.name
            )
        )


        //专辑 歌手 中间表
        albumArtistCrossRefDao.insertAll(
            albumResponse.album.artists.mapIndexed { index, singer ->
                AlbumArtistCrossRef(album.albumId, singer.id, index)
            }
        )


        toggleCollectAlbum(album.albumId, collected)

    }


    suspend fun toggleCollectAlbum(albumId: Long, collected: Boolean) {

        val currentUserId = userIdRepository.userId()

        if (collected) {
            //当前用户收藏了专辑
            userAlbumCrossRefDao.insert(
                UserAlbumCrossRef(currentUserId, albumId)
            )
        } else {
            userAlbumCrossRefDao.deleteByUserIdAndAlbumId(currentUserId, albumId)
        }
    }

}