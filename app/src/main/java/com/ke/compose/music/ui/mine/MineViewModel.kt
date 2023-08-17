package com.ke.compose.music.ui.mine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.api.response.UserDetailResponse
import com.ke.music.common.domain.GetCurrentUserDetailUseCase
import com.ke.music.common.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MineViewModel @Inject constructor(
    private val getUserDetailUseCase: GetCurrentUserDetailUseCase,
) : ViewModel() {

    private val _refreshing = MutableStateFlow(false)

    internal val refreshing: StateFlow<Boolean>
        get() = _refreshing

    private val _currentUser = MutableStateFlow<MineUiState?>(null)

    internal val currentUser: StateFlow<MineUiState?>
        get() = _currentUser

    init {
        refresh()
    }

    internal fun refresh() {
        viewModelScope.launch {
            _refreshing.value = true

            when (val result = getUserDetailUseCase(Unit)) {
                is Result.Error -> {
                    _currentUser.value = null
                }

                is Result.Success -> {
                    _currentUser.value = result.data.mineUiState
                }
            }
            _refreshing.value = false
        }
    }

}

private val UserDetailResponse.mineUiState: MineUiState
    get() {
        return MineUiState(
            profile.nickname,
            profile.avatarUrl,
            level
        )
    }