package com.ke.music.tv.ui.playlist_detail

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


    internal val id = savedStateHandle.get<String>("id")!!.toLong()

    @OptIn(ExperimentalCoroutinesApi::class)
    internal val uiState =
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

    private fun loadData() {
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

    internal fun toggleBooked(detail: PlaylistDetailUiState) {
//        _uiState.value = detail.copy(
//            subscribed = !detail.subscribed,
//            bookedCount = detail.bookedCount + if (detail.subscribed) -1 else 1
//        )
//        viewModelScope.launch {
//            togglePlaylistSubscribedUseCase(detail.playlist.id to !detail.subscribed)
//        }
        viewModelScope.launch {
            togglePlaylistSubscribedUseCase(id to !detail.subscribed)
        }
    }


}