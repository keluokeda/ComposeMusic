package com.ke.compose.music.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * 全局ViewModel
 */
@HiltViewModel
class AppViewModel @Inject constructor(
) : ViewModel(), IAppViewModel {
}

interface IAppViewModel {


}

private val defaultAppViewModel = object : IAppViewModel {

}

val LocalAppViewModel = staticCompositionLocalOf<IAppViewModel> {
    defaultAppViewModel
}