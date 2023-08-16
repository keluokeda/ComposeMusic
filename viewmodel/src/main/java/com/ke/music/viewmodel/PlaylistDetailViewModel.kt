package com.ke.music.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.common.domain.FollowPlaylistUseCase
import com.ke.music.common.domain.LoadPlaylistDetailUseCase
import com.ke.music.common.entity.IPlaylist
import com.ke.music.common.entity.ISongEntity
import com.ke.music.common.entity.IUser
import com.ke.music.common.repository.CurrentUserRepository
import com.ke.music.common.repository.PlaylistRepository
import com.ke.music.common.repository.SongRepository
import com.ke.music.common.repository.UserRepository
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
    private val followPlaylistUseCase: FollowPlaylistUseCase,
    savedStateHandle: SavedStateHandle,
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository,
    userIdRepository: CurrentUserRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    val userId = userIdRepository.userIdFlow.stateIn(viewModelScope, SharingStarted.Eagerly, 0)


    val id = savedStateHandle.get<Long>("id")!!

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState =
        userIdRepository.userIdFlow.flatMapLatest { userId ->
            songRepository.querySongsByPlaylistId(id)
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
            loadPlaylistDetailUseCase(id)
//            when (val result = loadPlaylistDetailUseCase(id)) {
//                is Result.Error -> {
//                    result.exception.printStackTrace()
//                }
//
//                is Result.Success -> {
//
//                }
//            }
        }
    }

    fun toggleBooked(playlistId: Long) {
        viewModelScope.launch {
            followPlaylistUseCase(playlistId)
        }
    }


}

data class PlaylistDetailUiState(
    val playlist: IPlaylist?,
    val songs: List<ISongEntity>,
    val subscribed: Boolean,
    val creator: IUser?,
) {
    val hasData: Boolean
        get() = playlist != null && creator != null
}