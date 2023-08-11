package com.ke.music.tv.ui.artist_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.MusicRepository
import com.ke.music.repository.domain.LoadArtistMusicListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistHotSongsViewModel @Inject constructor(
    private val loadArtistMusicListUseCase: LoadArtistMusicListUseCase,
    savedStateHandle: SavedStateHandle,
    private val musicRepository: MusicRepository
) : ViewModel() {
    private val artistId = savedStateHandle.get<Long>("id")!!

    internal val hotSongs = musicRepository.artistHotSongs(artistId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch {
            loadArtistMusicListUseCase(artistId)
        }
    }
}