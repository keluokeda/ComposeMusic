package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.common.repository.CurrentUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    currentUserRepository: CurrentUserRepository,
) :
    ViewModel() {

    private val _navigationActions = Channel<Boolean>(capacity = Channel.CONFLATED)

    val navigationActions: Flow<Boolean>
        get() = _navigationActions.receiveAsFlow()

    init {
        viewModelScope.launch {
            currentUserRepository.userIdFlow.collect {
                _navigationActions.send(it != 0L)
            }
        }

    }


}