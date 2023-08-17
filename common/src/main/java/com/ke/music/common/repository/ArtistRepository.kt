package com.ke.music.common.repository

import androidx.paging.PagingSource
import com.ke.music.api.response.Artist
import com.ke.music.common.entity.IArtist
import com.ke.music.common.entity.IArtistDescription
import kotlinx.coroutines.flow.Flow

interface ArtistRepository {

    /**
     * 重置歌手简介
     */
    suspend fun resetArtistDescription(
        id: Long, list: List<IArtistDescription>,
    )

    /**
     * 获取某个歌手的简介列表
     */
    fun getDescriptionsByArtistId(artistId: Long): Flow<List<IArtistDescription>>


    /**
     * 查询歌手信息
     */
    fun getArtist(artistId: Long): Flow<Pair<IArtist?, Boolean>>


    /**
     * 重置歌手的热门歌曲
     */
    suspend fun resetArtistHotSongs(artistId: Long, songs: List<Long>)


    /**
     * 设置当前用户是否关注了歌手
     */
    suspend fun setCurrentUserFollowArtist(artistId: Long, followed: Boolean)

    /**
     * 保存单个歌手
     */
    suspend fun saveArtist(iArtist: IArtist)


    /**
     * 保存热门歌手
     */
    suspend fun saveHotArtists(area: Int, type: Int, list: List<Artist>, deleteOld: Boolean)


    fun hotArtists(type: Int, area: Int): PagingSource<Int, out IArtist>
}