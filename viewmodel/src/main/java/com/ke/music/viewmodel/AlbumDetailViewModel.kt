package com.ke.music.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.common.domain.CollectAlbumUseCase
import com.ke.music.common.entity.IAlbumEntity
import com.ke.music.common.entity.ISongEntity
import com.ke.music.common.repository.AlbumRepository
import com.ke.music.common.repository.SongRepository
import com.ke.music.repository.domain.LoadAlbumDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumDetailViewModel @Inject constructor(
    private val loadAlbumDetailUseCase: LoadAlbumDetailUseCase,
    private val collectAlbumUseCase: CollectAlbumUseCase,
    savedStateHandle: SavedStateHandle,
    songRepository: SongRepository,
    albumRepository: AlbumRepository,
) :
    ViewModel() {

    val id = savedStateHandle.get<Long>("id")!!


    val uiState = albumRepository.getAlbumEntity(id)
        .combine(
            songRepository.querySongsByAlbumId(id)
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
            collectAlbumUseCase(id to !collected)
        }

    }

    private fun loadDetail() {
        viewModelScope.launch {
            loadAlbumDetailUseCase(id)
        }
    }
}

data class AlbumDetailUiState(
    val albumEntity: IAlbumEntity?,
    val songs: List<ISongEntity>,
) {
    val hasData: Boolean
        get() = albumEntity != null && songs.isNotEmpty()
}