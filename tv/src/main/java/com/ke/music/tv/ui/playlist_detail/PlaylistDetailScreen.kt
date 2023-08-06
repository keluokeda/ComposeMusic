package com.ke.music.tv.ui.playlist_detail

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.music.download.LocalDownloadManager
import com.ke.music.room.db.entity.Playlist
import com.ke.music.room.entity.MusicEntity
import com.ke.music.tv.ui.components.MusicView

@Composable
fun PlaylistDetailRoute(
    onCommentButtonClick: (Long) -> Unit,
    onPlaylistSubscribersClick: (Long) -> Unit,
    onCoverImageClick: (Playlist) -> Unit,
) {

    val viewModel: PlaylistDetailViewModel = hiltViewModel()


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PlaylistDetailScreen(
        uiState = uiState,
        onDeleteMusic = {

        },
        onCommentButtonClick,
        onCoverImageClick,
    ) {
        viewModel.toggleBooked(it)
    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun PlaylistDetailScreen(
    uiState: PlaylistDetailUiState,
    onDeleteMusic: (MusicEntity) -> Unit,
    onCommentButtonClick: (Long) -> Unit,
    onCoverImageClick: (Playlist) -> Unit,
    bookClick: (PlaylistDetailUiState) -> Unit,
) {


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (uiState.hasData) {

            var imageUrl by remember {
                mutableStateOf(uiState.playlist!!.coverImgUrl)
            }

            Row {

                Spacer(modifier = Modifier.width(16.dp))


                Column(
                    modifier = Modifier
                        .width(300.dp)
                        .padding(vertical = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {

                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(300.dp)
                            .clickable {
                                onCoverImageClick(uiState.playlist!!)
                            }
                    )

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier
                            .width(300.dp)
                            .padding(
                                vertical = 8.dp
                            )
                    ) {
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null)
                        }
                        IconButton(onClick = {
                            onCommentButtonClick(uiState.playlist!!.id)
                        }) {
                            Icon(imageVector = Icons.Default.Comment, contentDescription = null)
                        }
                        IconButton(onClick = {
                            bookClick(uiState)
                        }) {
                            Icon(
                                imageVector = if (uiState.subscribed) Icons.Default.BookmarkAdded else Icons.Default.BookmarkAdd,
                                contentDescription = null
                            )
                        }
                        val downloadManager = LocalDownloadManager.current
                        IconButton(onClick = {
                            downloadManager.downloadPlaylist(uiState.playlist!!.id)
                        }) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = null)
                        }
                    }

                    Text(text = uiState.playlist!!.name, style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.playlist.description ?: "",
                        maxLines = 1,
                        style = MaterialTheme.typography.bodySmall
                    )


                }

                Spacer(modifier = Modifier.width(16.dp))

                TvLazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
//                    val imageSize = 100
//                    item {
//                        Row(
//                            modifier = Modifier.padding(
//                                start = 16.dp,
//                                top = 16.dp,
//                                end = 16.dp
//                            ),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            AsyncImage(
//                                model = uiState.playlist?.coverImgUrl,
//                                contentScale = ContentScale.Crop,
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .size(imageSize.dp)
//                                    .clickable {
//                                        uiState.playlist?.let { onCoverImageClick(it) }
//                                    }
//                                    .background(MaterialTheme.colorScheme.primary)
//                            )
//                            Spacer(modifier = Modifier.width(8.dp))
//
//                            Column {
//                                Text(text = uiState.playlist?.name ?: "", maxLines = 2)
//                                Spacer(modifier = Modifier.height(4.dp))
//
//                                Row(verticalAlignment = Alignment.CenterVertically) {
//                                    Avatar(
//                                        url = uiState.creator?.avatar ?: "",
//                                        size = 24
//                                    )
//                                    Spacer(modifier = Modifier.width(4.dp))
//                                    Text(
//                                        text = uiState.creator?.name ?: "",
//                                        style = MaterialTheme.typography.bodySmall,
//                                    )
//                                }
//                            }
//                        }
//
//                    }
//
//                    item {
//                        Text(
//                            text = uiState.playlist?.description ?: "",
//                            style = MaterialTheme.typography.bodySmall,
//                            maxLines = 1,
//                            modifier = Modifier
//                                .clickable {
//
//                                }
//                                .padding(horizontal = 16.dp, vertical = 8.dp)
//                        )
//                    }
//
//
//                    item {
//                        val navigationHandler = LocalNavigationHandler.current
//
//                        PlaylistDetailInfo(
//                            uiState.playlist?.shareCount ?: 0,
//                            uiState.playlist?.commentCount ?: 0,
//                            uiState.playlist?.bookedCount ?: 0,
//                            uiState.subscribed,
//                            {
//                                navigationHandler.navigate(
//                                    NavigationAction.NavigateToShare(
//                                        ShareType.Playlist,
//                                        uiState.playlist?.id ?: 0,
//                                        uiState.playlist?.name ?: "",
//                                        uiState.playlist?.description ?: "",
//                                        uiState.playlist?.coverImgUrl ?: ""
//                                    )
//                                )
//                            },
//                            {
//                                onCommentButtonClick(uiState.playlist?.id ?: 0)
//                            },
//                            {
//                                bookClick(uiState)
//                            },
//                            false
//                        )
//                    }
//
//                    item {
//                        Row(
//                            modifier = Modifier
//                                .padding(
//                                    top = 8.dp,
//                                    bottom = 8.dp,
//                                    start = 12.dp,
//                                    end = 16.dp
//                                ),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            IconButton(onClick = { /*TODO*/ }) {
//                                Icon(
//                                    imageVector = Icons.Outlined.PlayArrow,
//                                    contentDescription = null
//                                )
//                            }
//
//                            Text(
//                                text = "播放全部",
//                                modifier = Modifier
//                                    .weight(1f)
//                                    .padding(start = 4.dp)
//                            )
//                            IconButton(onClick = {
//                            }) {
//                                Icon(
//                                    imageVector = Icons.Default.Download,
//                                    contentDescription = null
//                                )
//                            }
//                        }
//                    }


                    items(
                        uiState.songs,
                        key = {
                            it.musicId
                        },
                    ) { musicEntity ->

                        MusicView(
                            uiState.songs.indexOf(musicEntity),
                            musicEntity = musicEntity,

                            onHasFocus = {
                                imageUrl = it.album.imageUrl
                            }


                        )
                    }


                }
            }


        } else {
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            Text(text = "加载中")
        }

    }
}

//
//@OptIn(ExperimentalTvMaterial3Api::class)
//@Composable
//private fun PlaylistSubscribersView(
//    userList: List<User>,
//    count: Int,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    if (userList.isEmpty()) {
//        return
//    }
//    Row(
//        modifier
//            .clickable {
//                onClick()
//            }
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        userList.take(5).forEach {
//            Box(modifier = Modifier.padding(end = 4.dp)) {
//                Avatar(
//                    url = it.avatarUrl, size = 40
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.weight(1f))
//        Text(text = "${count.niceCount()}人收藏", style = MaterialTheme.typography.bodySmall)
//        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
//    }
//}
//
//@Preview(showSystemUi = true)
//@Composable
//private fun PlaylistDetailScreenLoadingPreview() {
//    ComposeMusicTheme {
//        PlaylistDetailScreen(
//            uiState = PlaylistDetailUiState(null, emptyList(), false, null),
//
//            {},
//            {},
//            {},
//            {},
//            {},
//        )
//    }
//}


//
//@Preview(showSystemUi = true)
//@Composable
//private fun PlaylistDetailScreenContentPreview() {
//    val detail =
//        PlaylistDetailUiState.Detail(
//            1988,
//            2000,
//            109900,
//            3245,
//            mockSongList,
//            mockPlaylist.convert(),
//            false
//        )
//
//    ComposeMusicTheme {
//        PlaylistDetailScreen(uiState = detail, onBackButtonTap = {}, {}, {}, {}, {}, {}) {
//
//        }
//    }
//}

