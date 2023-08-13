package com.ke.music.tv.ui.play

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.ListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.music.player.service.LocalMusicPlayerController
import com.ke.music.room.entity.DownloadedMusicEntity
import com.ke.music.room.entity.QueryDownloadedMusicResult
import com.ke.music.tv.ui.theme.ComposeMusicTheme
import com.ke.music.viewmodel.LocalPlaylistSongsViewModel


@Composable
fun PlayRoute() {
    val musicPlayerController = LocalMusicPlayerController.current

    val currentPlayingSong by musicPlayerController.currentPlaying.collectAsStateWithLifecycle()
    val playing by musicPlayerController.isPlaying.collectAsStateWithLifecycle()
    val progress by musicPlayerController.progress.collectAsStateWithLifecycle()

    val viewModel: LocalPlaylistSongsViewModel = hiltViewModel()
    val songs by viewModel.songs.collectAsStateWithLifecycle()

    PlayScreen(currentPlayingSong, progress, playing, songs, {
        musicPlayerController.skipToNext()
    }, {
        musicPlayerController.skipToPrevious()
    }, {
        if (playing) {
            musicPlayerController.pause()
        } else {
            musicPlayerController.play()
        }
    }, {
        musicPlayerController.playMusic(it)
    }, {
        viewModel.removeSong(it)
    })
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun PlayScreen(
    currentPlayingSong: QueryDownloadedMusicResult?,
    progress: Pair<Long, Long>,
    playing: Boolean,
    songs: List<DownloadedMusicEntity>,
    skipNext: () -> Unit,
    skipPrevious: () -> Unit,
    playPause: () -> Unit,
    playNow: (Long) -> Unit,
    removeFromLocalPlaylist: (Long) -> Unit
) {


    Row(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                onClick = { }, modifier = Modifier
                    .fillMaxWidth(.8f)
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = currentPlayingSong?.albumImage,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()

                )
            }

            Text(
                text = currentPlayingSong?.name ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(text = currentPlayingSong?.albumName ?: "")




            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(onClick = {
                    skipPrevious()
                }) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious, contentDescription = null
                    )
                }

                IconButton(onClick = {
                    playPause()
                }) {
                    Icon(
                        imageVector = if (playing) Icons.Default.Pause else
                            Icons.Default.PlayArrow, contentDescription = null
                    )
                }

                IconButton(onClick = {
                    skipNext()
                }) {
                    Icon(
                        imageVector = Icons.Default.SkipNext, contentDescription = null
                    )
                }
            }

        }

        TvLazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(Color.Black)
        ) {


        }



        TvLazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            items(songs, key = {
                it.musicId
            }) {
                ListItem(selected = false,
                    onClick = {
                        playNow(it.musicId)
                    },
                    onLongClick = {
                        removeFromLocalPlaylist(it.musicId)
                    },
                    headlineContent = {
                        Text(text = it.name)
                    },
                    supportingContent = {
                        Text(text = it.albumName)
                    },
                    leadingContent = {
                        Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null)
                    })
            }
        }

    }
}

@Composable
@Preview
private fun PlayScreenPreview() {
    ComposeMusicTheme {
        PlayScreen(
            currentPlayingSong = QueryDownloadedMusicResult(0, "最爱", "周慧敏", "", ""),
            progress = 10000L to 20000,
            playing = true,
            songs = emptyList(),
            skipNext = { },
            skipPrevious = { },
            playPause = { },
            playNow = {},
            removeFromLocalPlaylist = {}
        )
    }
}