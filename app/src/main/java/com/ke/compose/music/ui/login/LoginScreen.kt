package com.ke.compose.music.ui.login

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.compose.music.observeWithLifecycle
import com.ke.compose.music.toast
import com.ke.compose.music.ui.theme.ComposeMusicTheme
import com.lightspark.composeqr.QrCodeView

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    toMainScreenAction: () -> Unit
) {
    val url by loginViewModel.qrUrl.collectAsStateWithLifecycle()

    val context = LocalContext.current.applicationContext

    loginViewModel.navigationActions.observeWithLifecycle {
//        context.toast("登录结果 $it")
        if (it) {
            toMainScreenAction()
        } else {
            context.toast("登录失败")
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen(
    url: String?,
    onLogin: () -> Unit,
    refresh: () -> Unit,
    width: Int = 250,
    loading: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "扫码登录")
            })
        }
    ) {
        Column(
            Modifier
                .padding(it)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))


            val buttonEnable = !loading && !url.isNullOrEmpty()

            QRImageView(url = url, size = width)

            Spacer(modifier = Modifier.height(32.dp))
            Button(enabled = buttonEnable, onClick = {
                onLogin()
            }, modifier = Modifier.width(width = width.dp)) {
                Text(text = "我已扫码")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(enabled = buttonEnable, onClick = {
                refresh()
            }, modifier = Modifier.width(width = width.dp)) {
                Text(text = "刷新二维码")
            }
        }
    }
}

@Composable
fun QRImageView(url: String?, size: Int) {
    if (url == null) {
        Text(
            text = "二维码加载失败",
            modifier = Modifier.size(size.dp),
            textAlign = TextAlign.Center
        )
    } else if (url.isEmpty()) {
        Box(modifier = Modifier.size(size.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        QrCodeView(data = url, modifier = Modifier.size(size.dp))
    }
}

@Preview(
    showBackground = true,
    device = Devices.NEXUS_6P,
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun LoginScreenPreview() {
    ComposeMusicTheme {
        LoginScreen{

        }
    }
}