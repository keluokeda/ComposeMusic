package com.ke.music.repository

import androidx.paging.PagingSource
import com.ke.music.common.entity.IArtist
import com.ke.music.common.entity.IArtistDescription
import com.ke.music.common.repository.ArtistRepository
import com.ke.music.common.repository.CurrentUserRepository
import com.ke.music.room.db.dao.ArtistDao
import com.ke.music.room.db.dao.ArtistDescriptionDao
import com.ke.music.room.db.dao.ArtistMusicCrossRefDao
import com.ke.music.room.db.dao.HotArtistCrossRefDao
import com.ke.music.room.db.dao.UserArtistCrossRefDao
import com.ke.music.room.db.entity.Artist
import com.ke.music.room.db.entity.ArtistDescription
import com.ke.music.room.db.entity.ArtistMusicCrossRef
import com.ke.music.room.db.entity.HotArtistCrossRef
import com.ke.music.room.db.entity.UserArtistCrossRef
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class ArtistRepositoryImpl @Inject constructor(
    private val artistDescriptionDao: ArtistDescriptionDao,
    private val artistDao: ArtistDao,
    private val userArtistCrossRefDao: UserArtistCrossRefDao,
    private val userIdRepository: CurrentUserRepository,
    private val artistMusicCrossRefDao: ArtistMusicCrossRefDao,
    private val hotArtistCrossRefDao: HotArtistCrossRefDao,
) :
    ArtistRepository {
    override suspend fun resetArtistDescription(id: Long, list: List<IArtistDescription>) {
        artistDescriptionDao.resetArtistDescription(id, list.map {
            it.run {
                ArtistDescription(0, artistId, title, content)
            }
        })
    }

    override fun getDescriptionsByArtistId(artistId: Long): Flow<List<IArtistDescription>> {
        return artistDescriptionDao.getListByArtistId(artistId)
    }

    override fun getArtist(artistId: Long): Flow<Pair<IArtist?, Boolean>> {
        return artistDao.getArtist(artistId).combine(
            userIdRepository.userIdFlow.flatMapLatest {
                userArtistCrossRefDao.isUserFollowArtist(it, artistId)
            }
        ) { artist, flow ->
            artist to (flow != null)
        }
    }


    override suspend fun resetArtistHotSongs(artistId: Long, songs: List<Long>) {
        artistMusicCrossRefDao.resetArtistHotSongs(
            artistId, songs.map {
                ArtistMusicCrossRef(0, artistId, it)
            }
        )
    }


    override suspend fun setCurrentUserFollowArtist(artistId: Long, followed: Boolean) {
        val userId = userIdRepository.userId()
        val entity = UserArtistCrossRef(userId, artistId)
        if (followed) {
            userArtistCrossRefDao.insert(
                entity
            )
        } else {
            userArtistCrossRefDao.delete(entity)
        }
    }

    override suspend fun saveArtist(iArtist: IArtist) {
        artistDao.insert(
            Artist(iArtist.artistId, iArtist.name, iArtist.avatar)
        )
    }

    override suspend fun saveHotArtists(
        area: Int,
        type: Int,
        list: List<com.ke.music.api.response.Artist>,
        deleteOld: Boolean,
    ) {

        //保存歌手信息


//        artistDao.insertAll(list.map {
//            Artist(it.id, it.name, it.avatar)
//        })

        artistDao.insertArtists(list)


        val target = list.map {
            HotArtistCrossRef(0, it.id, area, type)
        }
        if (deleteOld) {
            hotArtistCrossRefDao.deleteOldAndInsertNew(area, type, target)
        } else {
            //保存热门歌手记录
            hotArtistCrossRefDao.insertAll(
                target
            )
        }


    }

    override fun hotArtists(type: Int, area: Int): PagingSource<Int, out IArtist> {
        return hotArtistCrossRefDao.getHotArtists(area, type)
    }
}