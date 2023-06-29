package com.ke.compose.music.ui.playlist_new

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.compose.music.observeWithLifecycle
import com.ke.compose.music.toast
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.theme.ComposeMusicTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistNewScreen(
    onBackButtonClick: () -> Unit,
    onSavedSuccess: () -> Unit
) {


    val viewModel: PlaylistNewViewModel = hiltViewModel()

    val context = LocalContext.current.applicationContext

    viewModel.navigationActions.observeWithLifecycle {
        if (it) {
            onSavedSuccess()
        } else {
            context.toast("保存失败")
        }
    }

    val loading by viewModel.loading.collectAsStateWithLifecycle()

    var checked by remember {
        mutableStateOf(false)
    }
    var text by remember {
        mutableStateOf("")
    }
    Scaffold(topBar = {
        AppTopBar(
            title = {
                Text(text = "新建歌单")
            }, navigationIcon = {
                IconButton(onClick = onBackButtonClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
    }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(it),
        ) {
            TextField(value = text, onValueChange = { string ->
                text = string
            }, label = {
                Text(text = "请输入歌单标题")
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
                enabled = !loading
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(text = "设置为隐私歌单", modifier = Modifier.weight(1f))
                Switch(
                    enabled = !loading,
                    checked = checked,
                    onCheckedChange = { value -> checked = value })
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.commit(text, checked)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading && text.isNotEmpty()
                ) {
                    Text(text = "提交")
                }
            }

            if (loading) {
                CircularProgressIndicator()
            }

        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun PlaylistNewScreenPreview() {
    ComposeMusicTheme {
        PlaylistNewScreen({}, {})
    }
}