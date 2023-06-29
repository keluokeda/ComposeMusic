package com.ke.compose.music.ui.comments

import androidx.compose.runtime.staticCompositionLocalOf

fun interface MusicController {


    fun sendAction(action: MusicControllerAction)
}

sealed interface MusicControllerAction {
    data class PlayNow(val id: Long) : MusicControllerAction
}

val LocalMusicController = staticCompositionLocalOf {
    MusicController {

    }
}