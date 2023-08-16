package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.common.domain.LoadRecommendSongsUseCase
import com.ke.music.common.repository.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendSongsViewModel @Inject constructor(
    songRepository: SongRepository,
    loadRecommendSongsUseCase: LoadRecommendSongsUseCase,
) : ViewModel() {


    val songs = songRepository.getRecommendSongs()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch {
            loadRecommendSongsUseCase(Unit)
        }
    }
}