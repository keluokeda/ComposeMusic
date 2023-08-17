package com.ke.music.tv.ui.artist_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.common.domain.LoadArtistDetailUseCase
import com.ke.music.common.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistHotSongsViewModel @Inject constructor(
    private val loadArtistDetailUseCase: LoadArtistDetailUseCase,
    savedStateHandle: SavedStateHandle,
    private val songRepository: SongRepository,
) : ViewModel() {
    private val artistId = savedStateHandle.get<Long>("id")!!

    internal val hotSongs = songRepository.artistHotSongs(artistId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch {
            loadArtistDetailUseCase(artistId)
        }
    }
}