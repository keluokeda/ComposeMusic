package com.ke.compose.music.ui.playlist_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.LocalBackHandler
import com.ke.compose.music.ui.theme.ComposeMusicTheme
import com.ke.music.api.response.Playlist
import com.ke.music.api.response.mockPlaylist

@Composable
fun PlaylistListRoute(
    onSelected: (Long) -> Unit,
    onNewButtonClick: () -> Unit
) {
    val viewModel: PlaylistListViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PlaylistListScreen(
        uiState = uiState,
        retry = {
            viewModel.loadPlaylist()
        },
        onSelected = {
            onSelected(it.id)
        },
        onNewButtonClick = onNewButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun PlaylistListScreen(
    uiState: PlaylistListUiState,
    retry: () -> Unit,
    onSelected: (Playlist) -> Unit,
    onNewButtonClick: () -> Unit
) {
    Scaffold(topBar = {
        AppTopBar(
            title = { Text(text = "选择歌单") },
            actions = {
                IconButton(onClick = onNewButtonClick) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }, navigationIcon = {

                val backHandler = LocalBackHandler.current

                IconButton(onClick = {
                    backHandler.navigateBack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
    }) { paddingValues ->
        when (uiState) {
            is PlaylistListUiState.Data -> {
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    items(uiState.list, key = {
                        it.id
                    }) {
                        ListItem(icon = {
                            AsyncImage(
                                model = it.coverImgUrl,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                        }, modifier = Modifier.clickable {
                            onSelected(it)
                        }) {
                            Text(text = it.name)
                        }
                    }
                }
            }

            PlaylistListUiState.Error -> {


                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    OutlinedButton(onClick = retry) {
                        Text(text = "出错了，点我充数")
                    }
                }
            }

            PlaylistListUiState.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlaylistListScreenPreview() {
    ComposeMusicTheme {
        PlaylistListScreen(
            uiState = PlaylistListUiState.Data(listOf(mockPlaylist)),
            retry = { /*TODO*/ },
            onSelected = {}
        ) {

        }
    }
}