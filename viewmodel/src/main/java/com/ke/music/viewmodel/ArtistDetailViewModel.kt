package com.ke.music.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.AlbumRepository
import com.ke.music.repository.MusicRepository
import com.ke.music.repository.MvRepository
import com.ke.music.repository.domain.LoadArtistAlbumsUseCase
import com.ke.music.repository.domain.LoadArtistDescriptionUseCase
import com.ke.music.repository.domain.LoadArtistMusicListUseCase
import com.ke.music.repository.domain.LoadArtistMvsUseCase
import com.ke.music.repository.domain.successOr
import com.ke.music.room.db.dao.ArtistDescriptionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistDetailViewModel @Inject constructor(
    private val loadArtistDescriptionUseCase: LoadArtistDescriptionUseCase,
    private val loadArtistMusicListUseCase: LoadArtistMusicListUseCase,
    private val loadArtistAlbumsUseCase: LoadArtistAlbumsUseCase,
    private val loadArtistMvsUseCase: LoadArtistMvsUseCase,
    albumRepository: AlbumRepository,
    savedStateHandle: SavedStateHandle,
    artistDescriptionDao: ArtistDescriptionDao,
    musicRepository: MusicRepository,
    private val mvRepository: MvRepository

) :
    ViewModel() {

    private val artistId = savedStateHandle.get<Long>("id")!!

    private val _nameAvatar = MutableStateFlow("歌手详情" to "")

    val nameAvatar: StateFlow<Pair<String, String>>
        get() = _nameAvatar

    init {
        viewModelScope.launch {
            _nameAvatar.value = loadArtistMusicListUseCase(artistId).successOr("歌手详情" to "")
            loadArtistDescriptionUseCase(artistId)
            loadArtistAlbumsUseCase(artistId)
            loadArtistMvsUseCase(artistId)
        }
    }


    /**
     * 歌手描述
     */
    val artistDescriptions = artistDescriptionDao.getListByArtistId(artistId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /**
     * 歌手热门歌曲
     */
    val artistHotSongs = musicRepository.artistHotSongs(artistId)
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