package com.ke.music.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.common.domain.AddOrRemoveSongsToPlaylistUseCase
import com.ke.music.common.domain.successOr
import com.ke.music.common.entity.AddOrRemoveSongsToPlaylistRequest
import com.ke.music.common.repository.PlaylistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistListViewModel @Inject constructor(
    playlistRepository: PlaylistRepository,
    savedStateHandle: SavedStateHandle,
    private val addOrRemoveSongsToPlaylistUseCase: AddOrRemoveSongsToPlaylistUseCase,
) : ViewModel() {

    //目标歌曲id
    private val songId = savedStateHandle.get<Long>("id")!!
    val list = playlistRepository.getCurrentUserPlaylist(false)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _loading = MutableStateFlow(false)

    val loading: StateFlow<Boolean>
        get() = _loading

    private val _result = Channel<Boolean>(capacity = Channel.CONFLATED)
    val result: Flow<Boolean>
        get() = _result.receiveAsFlow()


    /**
     * 收藏歌曲到歌单
     */
    fun collectSongToPlaylist(playlistId: Long) {
        viewModelScope.launch {
            _loading.value = true
            val result = addOrRemoveSongsToPlaylistUseCase(
                AddOrRemoveSongsToPlaylistRequest(
                    playlistId, listOf(songId), true
                )
            ).successOr(false)

            _loading.value = false
            _result.send(result)
        }
    }
}