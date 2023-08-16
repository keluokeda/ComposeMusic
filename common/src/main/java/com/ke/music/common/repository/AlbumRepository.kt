package com.ke.music.common.repository

import androidx.paging.PagingSource
import com.ke.music.api.response.AlbumData
import com.ke.music.api.response.AlbumResponse
import com.ke.music.common.entity.IAlbum
import com.ke.music.common.entity.IAlbumEntity
import kotlinx.coroutines.flow.Flow

interface AlbumRepository {


    fun getAlbumEntity(albumId: Long): Flow<IAlbumEntity?>

    /**
     * 保存数据到数据库
     */
    suspend fun saveAlbumResponseToDatabase(albumResponse: AlbumResponse, collected: Boolean)

    /**
     * 切换收藏状态
     */
    suspend fun toggleCollectAlbum(albumId: Long, collected: Boolean)

    /**
     * 保存歌手的所有专辑
     */
    suspend fun saveArtistAlbums(artistId: Long, list: List<IAlbum>)

    /**
     * 获取歌手的所有专辑
     */
    fun getArtistAlbums(artistId: Long): Flow<List<IAlbum>>

    /**
     * 保存新专辑
     */
    suspend fun saveNewAlbum(list: List<AlbumData>, area: String, deleteOld: Boolean)

    fun getNewAlbums(area: String): PagingSource<Int, out IAlbum>
}