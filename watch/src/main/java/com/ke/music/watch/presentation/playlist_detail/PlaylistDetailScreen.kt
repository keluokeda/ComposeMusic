package com.ke.music.watch.presentation.playlist_detail

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.OutlinedChip
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.ke.music.download.LocalDownloadManager
import com.ke.music.player.service.LocalMusicPlayerController
import com.ke.music.room.db.entity.Download
import com.ke.music.viewmodel.PlaylistDetailViewModel

@Composable
fun PlaylistDetailRoute() {
    val viewModel: PlaylistDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold {

        val downloadManager = LocalDownloadManager.current
        val playerController = LocalMusicPlayerController.current


        if (uiState.hasData) {
            ScalingLazyColumn {

                item {
                    OutlinedChip(label = {
                        Text(text = "全部下载")
                    }, onClick = {
                        downloadManager.downloadPlaylist(uiState.playlist!!.id)
                    }, modifier = Modifier.fillMaxWidth())
                }

                items(uiState.songs, key = {
                    it.musicId
                }) {
                    Chip(label = {
                        Text(text = it.name)
                    }, icon = {
                        val image = when (it.downloadStatus) {
                            Download.STATUS_DOWNLOADED -> {
                                Icons.Default.PlayCircle
                            }

                            Download.STATUS_DOWNLOAD_ERROR -> {
                                Icons.Default.Refresh
                            }

                            Download.STATUS_DOWNLOADING -> {
                                Icons.Default.Downloading
                            }

                            else -> {
                                Icons.Default.Download
                            }
                        }

                        Icon(imageVector = image, contentDescription = image.name)
                    }, modifier = Modifier.fillMaxWidth(), onClick = {
                        when (it.downloadStatus) {
                            Download.STATUS_DOWNLOADED -> {
                                playerController.playMusic(it.musicId)
                            }

                            Download.STATUS_DOWNLOAD_IDLE, null -> {
                                downloadManager.downloadMusic(it.musicId)
                            }

                            Download.STATUS_DOWNLOAD_ERROR -> {
                                downloadManager.retry(it.musicId)
                            }

                            else -> {

                            }
                        }
                    })
                }
            }
        } else {
            CircularProgressIndicator()
        }
    }
}