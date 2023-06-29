package com.ke.compose.music.ui.playlist_new

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.compose.music.domain.successOr
import com.ke.compose.music.repository.UserIdRepository
import com.ke.compose.music.ui.playlist.LoadUserPlaylistUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistNewViewModel @Inject constructor(
    private val createPlaylistUseCase: CreatePlaylistUseCase,
    private val loadUserPlaylistUseCase: LoadUserPlaylistUseCase,
    private val userIdRepository: UserIdRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)

    internal val loading: StateFlow<Boolean>
        get() = _loading

    private val _navigationActions = Channel<Boolean>(capacity = Channel.CONFLATED)

    val navigationActions: Flow<Boolean>
        get() = _navigationActions.receiveAsFlow()

    internal fun commit(name: String, checked: Boolean) {
        viewModelScope.launch {
            _loading.value = true
            val result = createPlaylistUseCase(name to checked).successOr(false)
            if (result) {
                loadUserPlaylistUseCase(userIdRepository.userId())
            }
            _loading.value = false

            _navigationActions.send(result)
        }
    }
}