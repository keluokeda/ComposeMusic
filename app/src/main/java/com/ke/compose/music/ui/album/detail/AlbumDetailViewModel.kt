package com.ke.compose.music.ui.album.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.AlbumRepository
import com.ke.music.repository.MusicRepository
import com.ke.music.repository.domain.LoadAlbumDetailUseCase
import com.ke.music.repository.domain.ToggleCollectAlbumUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val loadAlbumDetailUseCase: LoadAlbumDetailUseCase,
    private val toggleCollectAlbumUseCase: ToggleCollectAlbumUseCase,
    savedStateHandle: SavedStateHandle,
    musicRepository: MusicRepository,
    albumRepository: AlbumRepository
) :
    ViewModel() {

    internal val id = savedStateHandle.get<Long>("id")!!


    internal val uiState = albumRepository.getAlbumEntity(id)
        .combine(
            musicRepository.queryMusicListByAlbumId(id)
        ) { entity, list ->
            AlbumDetailUiState(entity, list)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, AlbumDetailUiState(null, emptyList()))


    init {
        loadDetail()
    }


    /**
     * 切换收藏
     */
    internal fun toggleCollect() {
        val collected = uiState.value.albumEntity?.collected ?: return

        viewModelScope.launch {
            toggleCollectAlbumUseCase(id to !collected)
        }

    }

    private fun loadDetail() {
        viewModelScope.launch {
            loadAlbumDetailUseCase(id)
        }
    }
}