package com.ke.music.watch.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Icon
import com.ke.music.commom.observeWithLifecycle
import com.ke.music.viewmodel.SplashViewModel

@Composable
fun SplashRoute(
    next: (Boolean) -> Unit
) {
    val viewModel: SplashViewModel = hiltViewModel()

    viewModel.navigationActions.observeWithLifecycle {
        next(it)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = Icons.Default.MusicNote, contentDescription = null)
    }

}