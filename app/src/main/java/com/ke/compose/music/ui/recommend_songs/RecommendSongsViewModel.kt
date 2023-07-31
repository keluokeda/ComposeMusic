package com.ke.compose.music.ui.recommend_songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.compose.music.repository.MusicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendSongsViewModel @Inject constructor(
    musicRepository: MusicRepository,
    private val loadRecommendSongsUseCase: LoadRecommendSongsUseCase
) : ViewModel() {


    val songs = musicRepository.getRecommendSongs()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    init {
        viewModelScope.launch {
            loadRecommendSongsUseCase(Unit)
        }
    }
}