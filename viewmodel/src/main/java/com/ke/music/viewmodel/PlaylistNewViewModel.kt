package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.common.domain.CreatePlaylistUseCase
import com.ke.music.common.domain.LoadCurrentUserPlaylistUseCase
import com.ke.music.common.domain.successOr
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
    private val loadCurrentUserPlaylistUseCase: LoadCurrentUserPlaylistUseCase,
) : ViewModel() {

    private val _loading = MutableStateFlow(false)

    val loading: StateFlow<Boolean>
        get() = _loading

    private val _navigationActions = Channel<Boolean>(capacity = Channel.CONFLATED)

    val navigationActions: Flow<Boolean>
        get() = _navigationActions.receiveAsFlow()

    fun commit(name: String, checked: Boolean) {
        viewModelScope.launch {
            _loading.value = true
            val result = createPlaylistUseCase(name to checked).successOr(false)
            if (result) {
                loadCurrentUserPlaylistUseCase(Unit)
            }
            _loading.value = false
            _navigationActions.send(result)
        }
    }
}