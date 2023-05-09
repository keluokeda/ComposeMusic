package com.ke.compose.music.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ke.compose.music.ui.LocalAppViewModel
import com.ke.compose.music.ui.comments.CommentType
import com.ke.compose.music.ui.share.ShareType
import com.ke.compose.music.ui.theme.ComposeMusicTheme
import com.ke.music.api.response.Song
import com.ke.music.api.response.mockSong
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SongBottomSheetLayout(
    selectedSong: Song?,
    sheetState: ModalBottomSheetState,
    actions: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    val scope = rememberCoroutineScope()
    val navigationHandler = LocalNavigationHandler.current

    ModalBottomSheetLayout(sheetContent = {
        Column {
            ListItem(icon = {
                Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null)
            }) {
                Text(text = "下一首播放")
            }

            val appViewModel = LocalAppViewModel.current

            ListItem(icon = {
                Icon(imageVector = Icons.Default.LibraryAdd, contentDescription = null)
            }, modifier = Modifier.clickable {
                scope.launch {
                    sheetState.hide()
                    appViewModel.selectedSongList = listOf(selectedSong!!.id)
                    navigationHandler.navigate(
                        NavigationAction.NavigateToPlaylistList
                    )
                }

            }) {
                Text(text = "收藏到歌单")
            }


            ListItem(icon = {
                Icon(imageVector = Icons.Default.Comment, contentDescription = null)
            }, modifier = Modifier.clickable {

                scope.launch {
                    sheetState.hide()
                    navigationHandler.navigate(
                        NavigationAction.NavigateToComments(
                            CommentType.Music,
                            selectedSong!!.id
                        )
                    )
                }
            }) {
                Text(text = "评论")
            }

            ListItem(icon = {
                Icon(imageVector = Icons.Default.Share, contentDescription = null)
            }, modifier = Modifier.clickable {

                scope.launch {
                    sheetState.hide()
                    navigationHandler.navigate(
                        NavigationAction.NavigateToShare(
                            ShareType.Song,
                            selectedSong!!.id,
                            selectedSong.name,
                            selectedSong.subTitle,
                            selectedSong.album.imageUrl
                        )
                    )
                }

            }) {
                Text(text = "分享")
            }

            selectedSong?.singers?.forEach {
                ListItem(icon = {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                }) {
                    Text(text = "歌手：${it.name}", maxLines = 1)
                }
            }

            selectedSong?.album?.run {
                ListItem(icon = {
                    Icon(imageVector = Icons.Default.Album, contentDescription = null)
                }, modifier = Modifier.clickable {
                    scope.launch {
                        sheetState.hide()
                        navigationHandler.navigate(NavigationAction.NavigateToAlbumDetail(id))
                    }

                }) {
                    Text(text = "专辑：${name}", maxLines = 1)
                }
            }

            actions()

        }
    }, sheetState = sheetState, content = content)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SongBottomSheetLayoutPreview() {
    ComposeMusicTheme {


        val sheetState =
            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)
        SongBottomSheetLayout(
            selectedSong = mockSong,
            sheetState = sheetState,
            actions = { /*TODO*/ }) {
            Scaffold(topBar = {
                AppTopBar(title = { Text(text = "歌单") })
            }) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                }
            }
        }


    }
}