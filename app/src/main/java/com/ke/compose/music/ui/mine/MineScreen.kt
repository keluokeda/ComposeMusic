package com.ke.compose.music.ui.mine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.LocalNavigationHandler
import com.ke.compose.music.ui.component.NavigationAction
import com.ke.compose.music.ui.theme.ComposeMusicTheme

@Composable
fun MineRoute() {

    val viewModel: MineViewModel = hiltViewModel()
    val refreshing by viewModel.refreshing.collectAsStateWithLifecycle()

    val uiState by viewModel.currentUser.collectAsStateWithLifecycle()
    MineScreen(uiState, refreshing) {
        viewModel.refresh()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun MineScreen(
    uiState: MineUiState?,
    refreshing: Boolean,
    onRefresh: () -> Unit
) {

    val navigationHandler = LocalNavigationHandler.current


    val state = rememberPullRefreshState(refreshing, onRefresh)

    Scaffold(
        topBar = {
            AppTopBar(title = { Text(text = "我的") }, actions = {
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                }
            })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .pullRefresh(state)
        ) {

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .pullRefresh(state)
            ) {


                Card(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = uiState?.avatar,
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)

                        )

                        Text(
                            text = uiState?.name ?: "",
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = "lv.${uiState?.level}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                }


                ListItem(modifier = Modifier.clickable {

                }, icon = {
                    Icon(imageVector = Icons.Default.People, contentDescription = null)
                }) {
                    Text(text = "我的关注")
                }

                ListItem(modifier = Modifier.clickable {

                }, icon = {
                    Icon(imageVector = Icons.Outlined.People, contentDescription = null)
                }) {
                    Text(text = "我的粉丝")
                }

                ListItem(modifier = Modifier.clickable {

                }, icon = {
                    Icon(imageVector = Icons.Default.Cloud, contentDescription = null)
                }) {
                    Text(text = "我的云盘")
                }

                ListItem(modifier = Modifier.clickable {

                }, icon = {
                    Icon(imageVector = Icons.Default.History, contentDescription = null)
                }) {
                    Text(text = "最近播放")
                }

                ListItem(modifier = Modifier.clickable {
                    navigationHandler.navigate(NavigationAction.NavigateToDownloadingMusic)

                }, icon = {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null)
                }) {
                    Text(text = "下载中的音乐")
                }

                ListItem(modifier = Modifier.clickable {
                    navigationHandler.navigate(NavigationAction.NavigateToDownloadedMusic)

                }, icon = {
                    Icon(imageVector = Icons.Default.DownloadDone, contentDescription = null)
                }) {
                    Text(text = "已下载的音乐")
                }
            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = state,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
@Preview
private fun MineScreenPreview() {
    ComposeMusicTheme {
        MineScreen(null, true) {

        }
    }
}