package com.ke.compose.music.ui.playlist_detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.compose.music.niceCount
import com.ke.compose.music.ui.LocalAppViewModel
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.Avatar
import com.ke.compose.music.ui.component.LocalNavigationHandler
import com.ke.compose.music.ui.component.MusicBottomSheetLayout
import com.ke.compose.music.ui.component.MusicView
import com.ke.compose.music.ui.component.NavigationAction
import com.ke.compose.music.ui.theme.ComposeMusicTheme
import com.ke.music.api.response.User
import com.ke.music.download.LocalDownloadManager
import com.ke.music.repository.entity.ShareType
import com.ke.music.room.db.entity.Playlist
import com.ke.music.room.entity.MusicEntity
import kotlinx.coroutines.launch

@Composable
fun PlaylistDetailRoute(
    onBackButtonTap: () -> Unit,
    onCommentButtonClick: (Long) -> Unit,
    onPlaylistSubscribersClick: (Long) -> Unit,
    onCoverImageClick: (Playlist) -> Unit,
) {

    val viewModel: PlaylistDetailViewModel = hiltViewModel()


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val appViewModel = LocalAppViewModel.current
    PlaylistDetailScreen(
        uiState = uiState,
        onBackButtonTap,
        onDeleteMusic = {
            appViewModel.removeMusicsFromPlaylist(
                listOf(it.musicId), viewModel.id
            )
        },
        onCommentButtonClick,
        onCoverImageClick,
        onPlaylistSubscribersClick
    ) {
        viewModel.toggleBooked(it)
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
private fun PlaylistDetailScreen(
    uiState: PlaylistDetailUiState,
    onBackButtonTap: () -> Unit,
    onDeleteMusic: (MusicEntity) -> Unit,
    onCommentButtonClick: (Long) -> Unit,
    onCoverImageClick: (Playlist) -> Unit,
    onPlaylistSubscribersClick: (Long) -> Unit,
    bookClick: (PlaylistDetailUiState) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var selectedMusic by remember {
        mutableStateOf<MusicEntity?>(null)
    }

    val appViewModel = LocalAppViewModel.current

    MusicBottomSheetLayout(musicEntity = selectedMusic, sheetState, actions = {
        if ((uiState).playlist?.creatorId == appViewModel.currentUserId) {
            ListItem(icon = {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }, modifier = Modifier.clickable {
                scope.launch {
                    sheetState.hide()
                    onDeleteMusic(selectedMusic!!)
                }
            }) {
                Text(text = "删除")
            }
        }
    }) {
        Scaffold(
            topBar = {
                AppTopBar(title = {
                    Text(text = "歌单")
                }, navigationIcon = {
                    IconButton(onClick = onBackButtonTap) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                })
            }) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (uiState.hasData) {


                    LazyColumn {
                        val imageSize = 100
                        item {
                            Row(
                                modifier = Modifier.padding(
                                    start = 16.dp,
                                    top = 16.dp,
                                    end = 16.dp
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = uiState.playlist?.coverImgUrl,
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(imageSize.dp)
                                        .clickable {
                                            uiState.playlist?.let { onCoverImageClick(it) }
                                        }
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                                Spacer(modifier = Modifier.width(8.dp))

                                Column {
                                    Text(text = uiState.playlist?.name ?: "", maxLines = 2)
                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Avatar(
                                            url = uiState.creator?.avatar ?: "",
                                            size = 24
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = uiState.creator?.name ?: "",
                                            style = MaterialTheme.typography.bodySmall,
                                        )
                                    }
                                }
                            }

                        }

                        item {
                            Text(
                                text = uiState.playlist?.description ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                modifier = Modifier
                                    .clickable {

                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }


                        item {
                            val navigationHandler = LocalNavigationHandler.current

                            PlaylistDetailInfo(
                                uiState.playlist?.shareCount ?: 0,
                                uiState.playlist?.commentCount ?: 0,
                                uiState.playlist?.bookedCount ?: 0,
                                uiState.subscribed,
                                {
                                    navigationHandler.navigate(
                                        NavigationAction.NavigateToShare(
                                            ShareType.Playlist,
                                            uiState.playlist?.id ?: 0,
                                            uiState.playlist?.name ?: "",
                                            uiState.playlist?.description ?: "",
                                            uiState.playlist?.coverImgUrl ?: ""
                                        )
                                    )
                                },
                                {
                                    onCommentButtonClick(uiState.playlist?.id ?: 0)
                                },
                                {
                                    bookClick(uiState)
                                },
                                appViewModel.currentUserId != uiState.playlist?.creatorId
                            )
                        }


                        stickyHeader {
                            Row(
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.background)
                                    .padding(
                                        top = 8.dp,
                                        bottom = 8.dp,
                                        start = 12.dp,
                                        end = 16.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Icon(
                                        imageVector = Icons.Outlined.PlayCircle,
                                        contentDescription = null
                                    )
                                }

                                Text(
                                    text = "播放全部",
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 4.dp)
                                )
                                val downloadManager = LocalDownloadManager.current
                                IconButton(onClick = {
                                    downloadManager.downloadPlaylist(uiState.playlist?.id ?: 0)
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Download,
                                        contentDescription = null
                                    )
                                }
                            }
                        }


                        items(uiState.songs, key = {
                            it.musicId
                        }) { musicEntity ->

                            MusicView(
                                musicEntity = musicEntity,
                                rightButton = {
                                    IconButton(onClick = {
                                        selectedMusic = musicEntity
                                        scope.launch {
                                            sheetState.show()
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = null
                                        )
                                    }
                                },


                                )
                        }



                        item {
                            PlaylistSubscribersView(
                                userList = emptyList(),
                                count = uiState.playlist?.bookedCount ?: 0, {
                                    onPlaylistSubscribersClick(uiState.playlist?.id ?: 0)
                                }
                            )
                        }
                    }
                } else {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

            }
        }
    }
}


/**
 * @param bookEnable 自己的不能点
 */
@Composable
private fun PlaylistDetailInfo(
    shareCount: Int,
    commentCount: Int,
    bookedCount: Int,
    subscribed: Boolean,
    shareClick: () -> Unit,
    commentClick: () -> Unit,
    bookClick: () -> Unit,
    bookEnable: Boolean
) {
    Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        OutlinedButton(
            onClick = shareClick,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp)

        ) {
            IconText(
                imageVector = Icons.Default.Share,
                text = shareCount.niceCount()
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedButton(
            onClick = commentClick,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            IconText(
                imageVector = Icons.Default.Comment,
                text = commentCount.niceCount()
            )
        }
        Spacer(modifier = Modifier.width(8.dp))


        OutlinedButton(
            onClick = bookClick,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(vertical = 8.dp),
            enabled = bookEnable

        ) {
            IconText(
                imageVector = if (subscribed) Icons.Default.BookmarkAdded else Icons.Default.BookmarkAdd,
                text = bookedCount.niceCount()
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun PlaylistDetailInfoSubscribedPreview() {
    PlaylistDetailInfo(
        shareCount = 100,
        commentCount = 200,
        bookedCount = 30000,
        subscribed = true,
        shareClick = { /*TODO*/ },
        commentClick = { /*TODO*/ },
        bookClick = {},
        bookEnable = true
    )
}

@Preview(showBackground = true)
@Composable
private fun PlaylistDetailInfoUnsubscribedPreview() {
    PlaylistDetailInfo(
        shareCount = 100,
        commentCount = 200,
        bookedCount = 30000,
        subscribed = false,
        shareClick = { /*TODO*/ },
        commentClick = { /*TODO*/ },
        bookClick = {},
        bookEnable = true
    )
}


@Preview(showBackground = true)
@Composable
private fun PlaylistDetailInfoDisableBookPreview() {
    PlaylistDetailInfo(
        shareCount = 100,
        commentCount = 200,
        bookedCount = 30000,
        subscribed = true,
        shareClick = { /*TODO*/ },
        commentClick = { /*TODO*/ },
        bookClick = {},
        bookEnable = false
    )
}

@Composable
private fun IconText(imageVector: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = imageVector, contentDescription = null)
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = text)
    }
}

@Composable
private fun PlaylistSubscribersView(
    userList: List<User>,
    count: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (userList.isEmpty()) {
        return
    }
    Row(
        modifier
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        userList.take(5).forEach {
            Box(modifier = Modifier.padding(end = 4.dp)) {
                Avatar(
                    url = it.avatarUrl, size = 40
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Text(text = "${count.niceCount()}人收藏", style = MaterialTheme.typography.bodySmall)
        Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PlaylistDetailScreenLoadingPreview() {
    ComposeMusicTheme {
        PlaylistDetailScreen(
            uiState = PlaylistDetailUiState(null, emptyList(), false, null),
            {},
            {},
            {},
            {},
            {},
            {},
        )
    }
}


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

