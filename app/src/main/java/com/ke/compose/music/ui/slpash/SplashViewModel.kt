package com.ke.compose.music.ui.slpash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.compose.music.domain.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(private val checkLoginStatusUseCase: CheckLoginStatusUseCase) :
    ViewModel() {

    private val _navigationActions = Channel<Boolean>(capacity = Channel.CONFLATED)

    internal val navigationActions: Flow<Boolean>
        get() = _navigationActions.receiveAsFlow()

    init {
        viewModelScope.launch {
            _navigationActions.send(checkLoginStatusUseCase(Unit).successOr(false))
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}