package com.ke.music.tv.ui.my_playlist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.PlaylistRepository
import com.ke.music.repository.UserIdRepository
import com.ke.music.repository.domain.AddOrRemoveSongsToPlaylistRequest
import com.ke.music.repository.domain.AddOrRemoveSongsToPlaylistUseCase
import com.ke.music.repository.domain.LoadUserPlaylistUseCase
import com.ke.music.repository.domain.successOr
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
internal class MyPlaylistViewModel @Inject constructor(
    playlistRepository: PlaylistRepository,
    private val loadUserPlaylistUseCase: LoadUserPlaylistUseCase,
    private val userIdRepository: UserIdRepository,
    savedStateHandle: SavedStateHandle,
    private val addOrRemoveSongsToPlaylistUseCase: AddOrRemoveSongsToPlaylistUseCase
) : ViewModel() {

    private val songId = savedStateHandle.get<Long>("id")!!

    internal val list = playlistRepository.getCurrentUserPlaylist()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    private val _loading = MutableStateFlow(false)

    internal val loading: StateFlow<Boolean>
        get() = _loading


    private val _navigationActions = Channel<Boolean>(capacity = Channel.CONFLATED)

    val navigationActions: Flow<Boolean>
        get() = _navigationActions.receiveAsFlow()


    init {
        viewModelScope.launch {
            val userId = userIdRepository.userId()
            loadUserPlaylistUseCase(userId)
        }
    }

    internal fun addToPlaylist(playlistId: Long) {
        viewModelScope.launch {
            _loading.value = true
            val request = AddOrRemoveSongsToPlaylistRequest(playlistId, listOf(songId), true)
            val result = addOrRemoveSongsToPlaylistUseCase(request).successOr(false)
            _loading.value = true
            _navigationActions.send(result)
        }
    }

}