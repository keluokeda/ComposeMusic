package com.ke.music.watch.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.Scaffold
import com.ke.music.common.observeWithLifecycle
import com.ke.music.viewmodel.LoginViewModel
import com.lightspark.composeqr.QrCodeView

@Composable
fun LoginRoute(
    loginResult: (Boolean) -> Unit
) {
    val viewModel: LoginViewModel = hiltViewModel()

    val content by viewModel.qrUrl.collectAsStateWithLifecycle()

    viewModel.navigationActions.observeWithLifecycle {
        loginResult(it)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.startAutoLogin(2000)
    }

    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            QrCodeView(data = content ?: "", modifier = Modifier.fillMaxSize(.7f))
        }
    }
}