package com.ke.music.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.common.domain.FollowArtistUseCase
import com.ke.music.common.domain.LoadArtistDetailUseCase
import com.ke.music.common.repository.AlbumRepository
import com.ke.music.common.repository.ArtistRepository
import com.ke.music.common.repository.MvRepository
import com.ke.music.common.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val loadArtistDetailUseCase: LoadArtistDetailUseCase,
    private val followArtistUseCase: FollowArtistUseCase,
    albumRepository: AlbumRepository,
    artistRepository: ArtistRepository,
    savedStateHandle: SavedStateHandle,
    songRepository: SongRepository,
    mvRepository: MvRepository,

    ) :
    ViewModel() {

    private val artistId = savedStateHandle.get<Long>("id")!!


    init {
        viewModelScope.launch {
            loadArtistDetailUseCase(artistId)
        }
    }


    /**
     * 关注歌手
     */
    fun followArtist(follow: Boolean) {
        viewModelScope.launch {
            followArtistUseCase(artistId to follow)
        }
    }


    /**
     * 歌手和是否关注
     */
    val artistAndFollowed = artistRepository.getArtist(artistId).stateIn(
        viewModelScope,
        SharingStarted.Eagerly, null to false
    )

    /**
     * 歌手描述
     */
    val artistDescriptions = artistRepository.getDescriptionsByArtistId(artistId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /**
     * 歌手热门歌曲
     */
    val artistHotSongs = songRepository.artistHotSongs(artistId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /**
     * 歌手所有专辑
     */
    val artistAlbums = albumRepository.getArtistAlbums(artistId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /**
     * 歌手mv
     */
    val artistMvs = mvRepository.getArtistMvs(artistId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

}