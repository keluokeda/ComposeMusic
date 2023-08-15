package com.ke.music.tv.ui.slpash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import com.ke.music.common.observeWithLifecycle

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SplashScreen(next: (Boolean) -> Unit) {
    val splashViewModel = hiltViewModel<SplashViewModel>()

    splashViewModel.navigationActions.observeWithLifecycle {
        next(it)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.White
        )

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SplashScreenPreview() {

    SplashScreen {

    }
}