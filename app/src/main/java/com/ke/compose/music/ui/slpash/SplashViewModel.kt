package com.ke.compose.music.ui.slpash

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.compose.music.userIdFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SplashViewModel @Inject constructor(
    @ApplicationContext context: Context
) :
    ViewModel() {

    private val _navigationActions = Channel<Boolean>(capacity = Channel.CONFLATED)

    internal val navigationActions: Flow<Boolean>
        get() = _navigationActions.receiveAsFlow()

    init {
        viewModelScope.launch {
            context.userIdFlow.collect {
                _navigationActions.send(it != 0L)
            }
        }
    }


}