package com.ke.music.common.repository

import com.ke.music.api.response.Song
import com.ke.music.common.entity.ISong
import com.ke.music.common.entity.ISongEntity
import kotlinx.coroutines.flow.Flow

interface SongRepository {


    /**
     * 保存歌曲保数据库
     */
    suspend fun saveSongs(list: List<Song>)

    /**
     * 查询歌单的所有歌曲
     */
    fun querySongsByPlaylistId(playlistId: Long): Flow<List<ISongEntity>>

    /**
     * 查询专辑的所有歌曲
     */
    fun querySongsByAlbumId(albumId: Long): Flow<List<ISongEntity>>

    /**
     * 查找已下载的歌曲
     */
    suspend fun findDownloadedSong(songId: Long): ISongEntity?

    /**
     * 获取已下载的歌曲
     */
    fun getDownloadedSongs(): Flow<List<ISongEntity>>

    /**
     * 推荐歌曲
     */
    fun getRecommendSongs(): Flow<List<ISongEntity>>


    /**
     * 保存每日推荐
     */
    suspend fun saveRecommendSongs(list: List<Song>)

    /**
     * 歌手热门歌曲
     */
    fun artistHotSongs(artistId: Long): Flow<List<ISongEntity>>

    /**
     * 音乐被播放的时候调用下这个方法
     */
    suspend fun onSongPlayed(songId: Long)

    /**
     * 插入一条歌曲到本地播放列表
     */
    suspend fun insertSongIntoLocalPlaylist(songId: Long)

    /**
     * 获取本地播放列表
     */
    fun getLocalPlaylistSongs(): Flow<List<ISongEntity>>

    /**
     * 从本地播放列表中移除一首歌曲
     */
    suspend fun removeSongFromLocalPlaylist(songId: Long)

    /**
     * 保存歌曲歌词
     */
    suspend fun saveSongLrc(songId: Long, lrc: String)

    /**
     * 获取歌曲歌词
     */
    suspend fun getSongLrc(songId: Long): String?
    suspend fun findById(musicId: Long): ISong?
    suspend fun deleteLocalPlaylistSong(songId: Long)
    fun getLocalPlaylistSongList(): Flow<List<ISongEntity>>
}