package com.ke.music.tv.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import com.ke.music.common.observeWithLifecycle
import com.ke.music.tv.ui.theme.ComposeMusicTheme
import com.ke.music.viewmodel.LoginViewModel
import com.lightspark.composeqr.QrCodeView

@Composable
fun LoginScreen(
    toMainScreenAction: () -> Unit
) {
    val loginViewModel: LoginViewModel = hiltViewModel()

    val url by loginViewModel.qrUrl.collectAsStateWithLifecycle()


    loginViewModel.navigationActions.observeWithLifecycle {
        if (it) {
            toMainScreenAction()
        } else {

        }
    }

    LoginScreen(
        url = url,
        onLogin = {
            loginViewModel.startLogin()
        },
        refresh = {
            loginViewModel.refresh()
        },
        loading = loginViewModel.loading.collectAsStateWithLifecycle().value
    )

}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun LoginScreen(
    url: String?,
    onLogin: () -> Unit,
    refresh: () -> Unit,
    width: Int = 250,
    loading: Boolean
) {

    Row(
        Modifier
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {

        QRImageView(url = url, size = width)

        Column(
            Modifier
                .height(width.dp)
                .padding(start = 32.dp),
            verticalArrangement = Arrangement.Center
        ) {

            val buttonEnable = !loading && !url.isNullOrEmpty()

            Button(enabled = buttonEnable, onClick = {
                onLogin()
            }, modifier = Modifier.width(width = width.dp)) {
                Text(text = "我已扫码")
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(enabled = buttonEnable, onClick = {
                refresh()
            }, modifier = Modifier.width(width = width.dp)) {
                Text(text = "刷新二维码")
            }

        }


    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun QRImageView(url: String?, size: Int) {

    Box(modifier = Modifier.size(size.dp), contentAlignment = Alignment.Center) {


        if (url == null) {
            Text(
                text = "二维码加载失败",
                textAlign = TextAlign.Center
            )
        } else if (url.isEmpty()) {
            Text(
                text = "二维码加载中",
                textAlign = TextAlign.Center
            )
        } else {
            QrCodeView(data = url, modifier = Modifier.size(size.dp))
        }
    }
}

@Preview(device = Devices.TV_1080p)
@Composable
private fun LoginScreenPreview() {
    ComposeMusicTheme {
        LoginScreen(url = "", onLogin = { /*TODO*/ }, refresh = { /*TODO*/ }, loading = false)
    }
}
