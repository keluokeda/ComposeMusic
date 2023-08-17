package com.ke.music.repository

import androidx.paging.PagingSource
import com.ke.music.common.repository.MvRepository
import com.ke.music.room.db.dao.AllMvDao
import com.ke.music.room.db.dao.MvArtistCrossRefDao
import com.ke.music.room.db.dao.MvDao
import com.ke.music.room.db.entity.AllMv
import com.ke.music.room.db.entity.Mv
import com.ke.music.room.db.entity.MvArtistCrossRef
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MvRepository @Inject constructor(
    private val mvDao: MvDao,
    private val allMvDao: AllMvDao,
    private val mvArtistCrossRefDao: MvArtistCrossRefDao,
) : MvRepository {

    /**
     * 保存歌手的mv
     */
    override suspend fun saveArtistMv(
        artistId: Long,
        list: List<com.ke.music.api.response.ArtistMv>,
    ) {
        mvDao.insertAll(list.map {
            Mv(it.id, it.name, it.image, it.playCount, it.duration, it.publishTime, it.artistName)
        })

        mvArtistCrossRefDao.resetArtistMv(
            artistId,
            list.mapIndexed { index, artistMv ->
                MvArtistCrossRef(artistId, artistMv.id, index)
            })

    }

    override suspend fun saveAllMv(
        area: String,
        type: String,
        list: List<com.ke.music.api.response.Mv>,
        deleteOld: Boolean,
    ) {
        mvDao.insertAll(
            list.map {
                Mv(it.id, it.name, it.cover, it.playCount, it.duration, "", it.artistName)
            }
        )

        val allMv = list.map {
            AllMv(id = 0, it.id, area, type)
        }
        if (deleteOld) {
            allMvDao.deleteOldAndInsertNew(area, type, allMv)
        } else {
            allMvDao.insertAll(allMv)
        }
    }

    override fun getArtistMvs(artistId: Long) = mvDao.getArtistMvs(artistId)


//    override fun  getAllMv(area: String, type: String): PagingSource<Int, Mv> {
//        return mvDao.getAllMvByAreaAndType(area, type)
//    }


    override fun getAllMv(area: String, type: String): PagingSource<Int, Mv> {
        return mvDao.getAllMvByAreaAndType(area, type)
    }

}