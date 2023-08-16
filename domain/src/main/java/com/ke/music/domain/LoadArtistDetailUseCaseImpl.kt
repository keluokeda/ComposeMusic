package com.ke.music.domain

import android.os.Parcel
import com.ke.music.api.HttpService
import com.ke.music.common.domain.LoadArtistDetailUseCase
import com.ke.music.common.entity.IAlbum
import com.ke.music.common.entity.IArtist
import com.ke.music.common.entity.IArtistDescription
import com.ke.music.common.repository.AlbumRepository
import com.ke.music.common.repository.ArtistRepository
import com.ke.music.common.repository.MvRepository
import com.ke.music.common.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

class LoadArtistDetailUseCaseImpl @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val songRepository: SongRepository,
    private val httpService: HttpService,
    private val albumRepository: AlbumRepository,
    private val mvRepository: MvRepository,
) : UseCase<Long, Unit>(Dispatchers.IO), LoadArtistDetailUseCase {
    override suspend fun execute(parameters: Long) {

        //歌手名称 头像 关注状态 热门歌曲
        loadArtistHotSongAndInfo(parameters)

        //歌手简介
        loadArtistDescriptions(parameters)

        loadArtistAlbums(parameters)

        val response = httpService.getArtistMv(parameters)

        mvRepository.saveArtistMv(parameters, response.mvs)
    }

    private suspend fun loadArtistAlbums(parameters: Long) {
        val response = httpService.getArtistAlbums(parameters)
        albumRepository.saveArtistAlbums(parameters, response.hotAlbums.map {
            Album(it.id, it.name, it.imageUrl)
        })
    }

    private suspend fun loadArtistHotSongAndInfo(parameters: Long) {
        httpService.getArtists(parameters).let {
            artistRepository.saveArtist(
                object : IArtist {
                    override val artistId: Long
                        get() = it.artist.id
                    override val name: String
                        get() = it.artist.name
                    override val avatar: String
                        get() = it.artist.avatar
                    override val key: Long
                        get() = 0

                    override fun describeContents(): Int {
                        return 0
                    }

                    override fun writeToParcel(dest: Parcel, flags: Int) {
                    }

                }
            )
            songRepository.saveSongs(it.hotSongs)
            artistRepository.resetArtistHotSongs(parameters, it.hotSongs.map { song -> song.id })
            artistRepository.setCurrentUserFollowArtist(parameters, it.artist.followed)
        }
    }


    private suspend fun loadArtistDescriptions(artistId: Long) {
        val response = httpService.getArtistDesc(artistId)

        val list = mutableListOf<ArtistDescription>()
        list.add(
            ArtistDescription(
                artistId = artistId,
                title = "简介",
                content = response.briefDesc
            )
        )

        response.introduction.forEach {
            list.add(
                ArtistDescription(
                    artistId = artistId,
                    title = it.ti,
                    content = it.txt
                )
            )
        }

        artistRepository.resetArtistDescription(artistId, list)
    }


}

private class ArtistDescription(
    override val artistId: Long,
    override val title: String,
    override val content: String,
    override val id: Long = 0,
) : IArtistDescription

@Parcelize
private class Album(
    override val albumId: Long,
    override val name: String,
    override val image: String,
) :
    IAlbum {
    override val key: Long
        get() = albumId
}