package com.ke.music.tv.ui.user_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.UserIdRepository
import com.ke.music.repository.domain.DeletePlaylistUseCase
import com.ke.music.repository.domain.LoadUserPlaylistUseCase
import com.ke.music.room.db.dao.PlaylistDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserPlaylistViewModel @Inject constructor(
    private val loadUserPlaylistUseCase: LoadUserPlaylistUseCase,
    private val deletePlaylistUseCase: DeletePlaylistUseCase,
    private val playlistDao: PlaylistDao,
    private val userIdRepository: UserIdRepository
) : ViewModel() {


    val playlistList =
        userIdRepository.userIdFlow.flatMapLatest { userId ->
            playlistDao.getUserCreatedPlaylist(userId).combine(
                playlistDao.getUserFollowingPlaylist(userId)
            ) { list1, list2 ->
                (list1 + list2)
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    private val _refreshing = MutableStateFlow(false)
    internal val refreshing: StateFlow<Boolean>
        get() = _refreshing

    init {

        refresh()
    }


    internal fun refresh() {
        viewModelScope.launch {
            _refreshing.value = true
            val userId = userIdRepository.userId()
            loadUserPlaylistUseCase(userId)
            _refreshing.value = false
        }
    }

    internal fun deletePlaylist(playlistId: Long) {

        viewModelScope.launch {
            deletePlaylistUseCase(playlistId)
        }
    }
}