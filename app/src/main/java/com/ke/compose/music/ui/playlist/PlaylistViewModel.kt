package com.ke.compose.music.ui.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.compose.music.domain.successOr
import com.ke.compose.music.event.UserPlaylistListUpdatedEvent
import com.ke.music.api.response.Playlist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    private val getUserPlaylistUseCase: GetUserPlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase
) : ViewModel() {
    private val _list = MutableStateFlow<List<Playlist>>(emptyList())

    internal val list: StateFlow<List<Playlist>>
        get() = _list

    private val _refreshing = MutableStateFlow(false)
    internal val refreshing: StateFlow<Boolean>
        get() = _refreshing

    init {
        EventBus.getDefault().register(this)
        refresh()
    }

    @Subscribe
    fun onUserPlaylistListUpdatedEvent(event: UserPlaylistListUpdatedEvent) {
        refresh()
    }

    override fun onCleared() {
        super.onCleared()
        EventBus.getDefault().unregister(this)
    }

    internal fun refresh() {
        viewModelScope.launch {
            _refreshing.value = true
            _list.value = getUserPlaylistUseCase(Unit).successOr(emptyList())
            _refreshing.value = false
        }
    }

    internal fun deletePlaylist(playlist: Playlist) {
        val newList = list.value.toMutableList()
        newList.remove(playlist)
        _list.value = newList
        viewModelScope.launch {
            deletePlaylistUseCase(playlist.id)
        }
    }
}