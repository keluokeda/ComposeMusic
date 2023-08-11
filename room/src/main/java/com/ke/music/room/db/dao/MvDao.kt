package com.ke.music.room.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.music.room.db.entity.Mv
import kotlinx.coroutines.flow.Flow

@Dao
interface MvDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<Mv>)


    /**
     * 查询歌手的所有mv
     */
    @Query("select mv.* from mv inner join mv_artist_cross_ref on mv.id = mv_artist_cross_ref.mv_id where artist_id = :artistId order by `index`")
    fun getArtistMvs(artistId: Long): Flow<List<Mv>>


    /**
     * 根据地区和类型查询所有mv
     */
    @Query("select mv.* from mv inner join all_mv on mv.id = all_mv.mv_id where area = :area and type = :type")
    fun getAllMvByAreaAndType(area: String, type: String): PagingSource<Int, Mv>
}