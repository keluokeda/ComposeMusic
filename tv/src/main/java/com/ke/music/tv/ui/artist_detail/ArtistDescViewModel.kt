package com.ke.music.tv.ui.artist_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.domain.LoadArtistDescriptionUseCase
import com.ke.music.room.db.dao.ArtistDescriptionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ArtistDescViewModel @Inject constructor(
    private val loadArtistDescriptionUseCase: LoadArtistDescriptionUseCase,
    artistDescriptionDao: ArtistDescriptionDao,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val artistId = savedStateHandle.get<Long>("id")!!

    internal val artistDescriptionList = artistDescriptionDao.getListByArtistId(artistId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    init {
        viewModelScope.launch {
            loadArtistDescriptionUseCase(artistId)
        }
    }
}