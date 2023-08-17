package com.ke.music.tv.ui.user_playlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.items
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.music.common.entity.IPlaylist
import com.ke.music.viewmodel.PlaylistViewModel


@Composable
fun UserPlaylistRoute(
    onItemClick: (IPlaylist) -> Unit,
) {
    val viewModel: PlaylistViewModel = hiltViewModel()

    val playlistList by viewModel.playlistList.collectAsStateWithLifecycle()

    UserPlaylistScreen(
        playlistList = playlistList,
        onItemClick
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun UserPlaylistScreen(
    playlistList: List<IPlaylist>,
    onItemClick: (IPlaylist) -> Unit,
) {


    TvLazyVerticalGrid(columns = TvGridCells.Fixed(4), contentPadding = PaddingValues(32.dp)) {
        items(playlistList, key = {
            it.id
        }) {
            Box(modifier = Modifier.padding(16.dp)) {
                Card(onClick = {
                    onItemClick(it)
                }, onLongClick = {
//                    onDeletePlaylistClick(it)

                }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        AsyncImage(
                            model = it.coverImgUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )

                        Text(
                            text = it.name,
                            maxLines = 1,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color.Black.copy(
                                        alpha = 0.3f
                                    )
                                )
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }

//    LazyColumn {
//        items(playlistList, key = { playlist ->
//            playlist.id
//        }) { playlist ->
//            PlaylistItem(playlist = playlist, onClick = {
//                onItemClick(playlist)
//            }, onDeletePlaylistClick)
//        }
//    }


}
//
//@Composable
//private fun PlaylistItem(
//    playlist: Playlist,
//    onClick: (Playlist) -> Unit,
//    onDeletePlaylistClick: (Playlist) -> Unit,
//    modifier: Modifier = Modifier,
//) {
//
//    var expanded by remember {
//        mutableStateOf(false)
//    }
//
//    val viewModel = LocalAppViewModel.current
//
//    Column {
//
//        Row(modifier = modifier
//            .clickable {
//                onClick(playlist)
//            }
//            .padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
//            verticalAlignment = Alignment.CenterVertically) {
//            AsyncImage(
//                model = playlist.coverImgUrl,
//                contentDescription = null,
//                modifier = Modifier.size(40.dp)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//
//            Column(modifier = Modifier.weight(1f)) {
//                Text(text = playlist.name, maxLines = 1)
//                Text(text = "${playlist.trackCount}首", style = MaterialTheme.typography.bodySmall)
//            }
//
//
//            Box(contentAlignment = Alignment.Center) {
//                IconButton(onClick = { expanded = true }) {
//                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
//                }
//                DropdownMenu(expanded = expanded, onDismissRequest = {
//                    expanded = false
//                }) {
//                    val downloadManager = LocalDownloadManager.current
//                    DropdownMenuItem(text = { Text(text = "下载") }, onClick = {
//                        downloadManager.downloadPlaylist(playlist.id)
//                        expanded = false
//                    })
//                    val navigationHandler = LocalNavigationHandler.current
//                    DropdownMenuItem(text = { Text(text = "分享") }, onClick = {
//                        navigationHandler.navigate(
//                            NavigationAction.NavigateToShare(
//                                ShareType.Playlist,
//                                playlist.id,
//                                playlist.name,
//                                playlist.description ?: "",
//                                playlist.coverImgUrl
//                            )
//                        )
//                        expanded = false
//                    })
//                    DropdownMenuItem(text = { Text(text = "删除") }, onClick = {
//                        expanded = false
//                        onDeletePlaylistClick(playlist)
//                    })
//
//                    val appViewModel = LocalAppViewModel.current
//
//                    if (playlist.creatorId == appViewModel.currentUserId)
//                        DropdownMenuItem(text = { Text(text = "编辑") }, onClick = {
//                            expanded = false
//                        })
//                }
//            }
//        }
//
//        Divider(startIndent = 16.dp)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//private fun PlaylistItemPreview() {
//    val playlist = mockPlaylist.convert()
//
//    PlaylistItem(playlist = playlist, onClick = {}, {})
//}
//
//@Preview(showBackground = true)
//@Composable
//private fun PlaylistItemShowMenuPreview() {
//    val playlist = mockPlaylist.convert()
//
//    PlaylistItem(playlist = playlist, onClick = {}, {})
//}