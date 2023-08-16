package com.ke.music.common.repository

import androidx.paging.PagingSource
import com.ke.music.api.response.ArtistMv
import com.ke.music.api.response.Mv
import com.ke.music.common.entity.IMv
import kotlinx.coroutines.flow.Flow

interface MvRepository {


    /**
     * 保存歌手mv
     */
    suspend fun saveArtistMv(artistId: Long, list: List<ArtistMv>)

    /**
     * 保存全部mv
     */
    suspend fun saveAllMv(area: String, type: String, list: List<Mv>, deleteOld: Boolean)

    /**
     * 获取歌手的mv
     */
    fun getArtistMvs(artistId: Long): Flow<List<IMv>>

    /**
     * 获取所有mv
     */
    fun getAllMv(
        area: String,
        type: String,
    ): PagingSource<Int, out IMv>
}