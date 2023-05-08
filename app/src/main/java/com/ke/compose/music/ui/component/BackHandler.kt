package com.ke.compose.music.ui.component

import androidx.compose.runtime.staticCompositionLocalOf

fun interface BackHandler {
    /**
     * 返回上一级
     */
    fun navigateBack()
}

val LocalBackHandler = staticCompositionLocalOf {
    BackHandler {

    }
}