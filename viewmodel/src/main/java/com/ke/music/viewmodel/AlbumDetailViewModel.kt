package com.ke.music.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.AlbumRepository
import com.ke.music.repository.MusicRepository
import com.ke.music.repository.domain.LoadAlbumDetailUseCase
import com.ke.music.repository.domain.ToggleCollectAlbumUseCase
import com.ke.music.room.entity.AlbumEntity
import com.ke.music.room.entity.MusicEntity
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

    val id = savedStateHandle.get<Long>("id")!!


    val uiState = albumRepository.getAlbumEntity(id)
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
    fun toggleCollect() {
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

data class AlbumDetailUiState(
    val albumEntity: AlbumEntity?,
    val musicList: List<MusicEntity>
) {
    val hasData: Boolean
        get() = albumEntity != null && musicList.isNotEmpty()
}