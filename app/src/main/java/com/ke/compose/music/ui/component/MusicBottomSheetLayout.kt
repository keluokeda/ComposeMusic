package com.ke.compose.music.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ke.compose.music.ui.LocalAppViewModel
import com.ke.music.common.entity.CommentType
import com.ke.music.download.LocalDownloadManager
import com.ke.music.repository.entity.ShareType
import com.ke.music.room.entity.MusicEntity
import kotlinx.coroutines.launch


@Deprecated("弃用了")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MusicBottomSheetLayout(
    musicEntity: MusicEntity?,
    sheetState: ModalBottomSheetState,
    actions: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    val scope = rememberCoroutineScope()
    val navigationHandler = LocalNavigationHandler.current

    ModalBottomSheetLayout(sheetContent = {
        Column(
            modifier = Modifier.padding(
                bottom = 32.dp
            )
        ) {
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
                    appViewModel.selectedSongList = listOf(musicEntity!!.musicId)
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
                            musicEntity!!.musicId
                        )
                    )
                }
            }) {
                Text(text = "评论")
            }

            val downloadManager = LocalDownloadManager.current
            ListItem(icon = {
                Icon(imageVector = Icons.Default.Download, contentDescription = null)
            }, modifier = Modifier.clickable {
                scope.launch {
                    sheetState.hide()
                }
                downloadManager.downloadMusic(musicEntity!!.musicId)
            }) {


                Text(text = "下载")
            }

            ListItem(icon = {
                Icon(imageVector = Icons.Default.Share, contentDescription = null)
            }, modifier = Modifier.clickable {

                scope.launch {
                    sheetState.hide()
                    navigationHandler.navigate(
                        NavigationAction.NavigateToShare(
                            ShareType.Song,
                            musicEntity!!.musicId,
                            musicEntity.name,
                            musicEntity.album.name,
                            musicEntity.album.imageUrl
                        )
                    )
                }

            }) {
                Text(text = "分享")
            }

            musicEntity?.artists?.forEach {
                ListItem(icon = {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                }) {
                    Text(text = "歌手：${it.name}", maxLines = 1)
                }
            }

            musicEntity?.album?.run {
                ListItem(icon = {
                    Icon(imageVector = Icons.Default.Album, contentDescription = null)
                }, modifier = Modifier.clickable {
                    scope.launch {
                        sheetState.hide()
                        navigationHandler.navigate(NavigationAction.NavigateToAlbumDetail(albumId))
                    }

                }) {
                    Text(text = "专辑：${name}", maxLines = 1)
                }
            }

            actions()

        }
    }, sheetState = sheetState, content = content)
}

