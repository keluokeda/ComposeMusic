package com.ke.music.pad.ui.playlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.music.api.response.mockPlaylist
import com.ke.music.common.entity.ShareType
import com.ke.music.download.LocalDownloadManager
import com.ke.music.pad.ui.component.LocalNavigationHandler
import com.ke.music.pad.ui.component.NavigationAction
import com.ke.music.repository.convert
import com.ke.music.room.db.entity.Playlist


@Composable
fun PlaylistRoute(

) {
    val viewModel: PlaylistViewModel = hiltViewModel()

    val playlistList by viewModel.playlistList.collectAsStateWithLifecycle()

    PlaylistScreen(
        playlistList = playlistList,
        refreshing = viewModel.refreshing.collectAsStateWithLifecycle().value,
        {
            viewModel.refresh()
        }, {}, { }, {
            viewModel.deletePlaylist(it.id)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun PlaylistScreen(
    playlistList: List<Playlist>,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    onItemClick: (Playlist) -> Unit,
    onNewPlaylistClick: () -> Unit,
    onDeletePlaylistClick: (Playlist) -> Unit
) {

    val state = rememberPullRefreshState(refreshing, onRefresh)

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(state)
        ) {

            LazyColumn {
                items(playlistList, key = { playlist ->
                    playlist.id
                }) { playlist ->
                    PlaylistItem(playlist = playlist, onClick = {
                        onItemClick(playlist)
                    }, onDeletePlaylistClick)
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
private fun PlaylistItem(
    playlist: Playlist,
    onClick: (Playlist) -> Unit,
    onDeletePlaylistClick: (Playlist) -> Unit,
    modifier: Modifier = Modifier,
) {

    var expanded by remember {
        mutableStateOf(false)
    }


    Column {

        Row(modifier = modifier
            .clickable {
                onClick(playlist)
            }
            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = playlist.coverImgUrl,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = playlist.name, maxLines = 1)
                Text(text = "${playlist.trackCount}首", style = MaterialTheme.typography.bodySmall)
            }


            Box(contentAlignment = Alignment.Center) {
                IconButton(onClick = { expanded = true }) {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = {
                    expanded = false
                }) {
                    val downloadManager = LocalDownloadManager.current
                    DropdownMenuItem(text = { Text(text = "下载") }, onClick = {
                        downloadManager.downloadPlaylist(playlist.id)
                        expanded = false
                    })
                    val navigationHandler = LocalNavigationHandler.current
                    DropdownMenuItem(text = { Text(text = "分享") }, onClick = {
                        navigationHandler.navigate(
                            NavigationAction.NavigateToShare(
                                ShareType.Playlist,
                                playlist.id,
                                playlist.name,
                                playlist.description ?: "",
                                playlist.coverImgUrl
                            )
                        )
                        expanded = false
                    })
                    DropdownMenuItem(text = { Text(text = "删除") }, onClick = {
                        expanded = false
                        onDeletePlaylistClick(playlist)
                    })

//                    val appViewModel = LocalAppViewModel.current
//
//                    if (playlist.creatorId == appViewModel.currentUserId)
//                        DropdownMenuItem(text = { Text(text = "编辑") }, onClick = {
//                            expanded = false
//                        })
                }
            }
        }

        Divider(startIndent = 16.dp)
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaylistItemPreview() {
    val playlist = mockPlaylist.convert()

    PlaylistItem(playlist = playlist, onClick = {}, {})
}

@Preview(showBackground = true)
@Composable
private fun PlaylistItemShowMenuPreview() {
    val playlist = mockPlaylist.convert()

    PlaylistItem(playlist = playlist, onClick = {}, {})
}