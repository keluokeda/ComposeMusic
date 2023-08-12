package com.ke.music.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.MusicRepository
import com.ke.music.repository.PlaylistRepository
import com.ke.music.repository.UserIdRepository
import com.ke.music.repository.UserRepository
import com.ke.music.repository.domain.LoadPlaylistDetailUseCase
import com.ke.music.repository.domain.Result
import com.ke.music.repository.domain.TogglePlaylistSubscribedUseCase
import com.ke.music.room.db.entity.Playlist
import com.ke.music.room.db.entity.User
import com.ke.music.room.entity.MusicEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailViewModel @Inject constructor(
    private val loadPlaylistDetailUseCase: LoadPlaylistDetailUseCase,
    private val togglePlaylistSubscribedUseCase: TogglePlaylistSubscribedUseCase,
    savedStateHandle: SavedStateHandle,
    private val musicRepository: MusicRepository,
    private val playlistRepository: PlaylistRepository,
    userIdRepository: UserIdRepository,
    private val userRepository: UserRepository
) : ViewModel() {


    val id = savedStateHandle.get<Long>("id")!!

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState =
        userIdRepository.userIdFlow.flatMapLatest { userId ->
            musicRepository.queryMusicListByPlaylistId(id)
                .combine(playlistRepository.findById(id)) { list, playlist -> list to playlist }
                .combine(
                    playlistRepository.checkUserHasSubscribePlaylist(
                        userId,
                        id
                    )
                ) { pair, subscribed ->

                    val user = userRepository.findById(pair.second?.creatorId ?: 0L)

                    PlaylistDetailUiState(
                        pair.second, pair.first, subscribed, user
                    )
                }

        }.stateIn(
            viewModelScope, SharingStarted.Eagerly, PlaylistDetailUiState(
                null,
                emptyList(), false, null
            )
        )

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            when (val result = loadPlaylistDetailUseCase(id)) {
                is Result.Error -> {
                    result.exception.printStackTrace()
                }

                is Result.Success -> {

                }
            }
        }
    }

    fun toggleBooked(detail: PlaylistDetailUiState) {
        viewModelScope.launch {
            togglePlaylistSubscribedUseCase(id to !detail.subscribed)
        }
    }


}

data class PlaylistDetailUiState(
    val playlist: Playlist?,
    val songs: List<MusicEntity>,
    val subscribed: Boolean,
    val creator: User?
) {
    val hasData: Boolean
        get() = playlist != null && creator != null
}