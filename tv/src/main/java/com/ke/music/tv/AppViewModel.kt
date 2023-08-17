package com.ke.music.tv

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.domain.AddOrRemoveSongsToPlaylistUseCase
import com.ke.music.repository.userIdFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 全局ViewModel
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    private val addOrRemoveSongsToPlaylistUseCase: AddOrRemoveSongsToPlaylistUseCase,
    @ApplicationContext private val context: Context
) : ViewModel(), IAppViewModel {

    private var _currentUserId = 0L

    override val currentUserId: Long
        get() = _currentUserId


    init {



        viewModelScope.launch {
            context.userIdFlow.collect {
                _currentUserId = it
            }
        }
    }






}

interface IAppViewModel {




    val currentUserId: Long
}

private val defaultAppViewModel = object : IAppViewModel {









    override val currentUserId: Long
        get() = 0L
}

val LocalAppViewModel = staticCompositionLocalOf<IAppViewModel> {
    defaultAppViewModel
}