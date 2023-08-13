package com.ke.compose.music.ui.play

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.compose.music.ui.theme.ComposeMusicTheme
import com.ke.music.player.service.LocalMusicPlayerController
import com.ke.music.room.entity.DownloadedMusicEntity
import com.ke.music.room.entity.QueryDownloadedMusicResult
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

@OptIn(ExperimentalMaterial3Api::class)
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
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Card {
                AsyncImage(
                    model = currentPlayingSong?.albumImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .aspectRatio(1f)
                )
            }



            Spacer(modifier = Modifier.height(32.dp))

            Text(text = currentPlayingSong?.name ?: "", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentPlayingSong?.albumName ?: "",
                style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center
            )


            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = if (progress.second == 0L) 0f else progress.first.toFloat() / progress.second,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = progress.first.niceTime, style = MaterialTheme.typography.bodySmall)
                Text(text = progress.second.niceTime, style = MaterialTheme.typography.bodySmall)
            }


            Spacer(modifier = Modifier.height(32.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val iconSize = 80.dp

                IconButton(onClick = skipPrevious) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = null,
                        modifier = Modifier.size(iconSize)
                    )
                }

                IconButton(onClick = playPause) {
                    Icon(
                        imageVector = if (playing) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(iconSize)
                    )
                }

                IconButton(onClick = skipNext) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = null,
                        modifier = Modifier.size(iconSize)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {


                items(songs, key = {
                    it.musicId
                }) {

                    val isCurrent = it.musicId == currentPlayingSong?.musicId


                    ListItem(modifier = Modifier.clickable {
                        playNow(it.musicId)
                    },

                        headlineContent = {
                            Text(
                                text = it.name, maxLines = 1,
                                style = if (isCurrent) LocalTextStyle.current.copy(
                                    androidx.compose.ui.graphics.Color.Red
                                ) else LocalTextStyle.current
                            )
                        }, supportingContent = {
                            Text(
                                text = it.albumName,
                                maxLines = 1,
                                style = if (isCurrent) LocalTextStyle.current.copy(
                                    androidx.compose.ui.graphics.Color.Red
                                ) else LocalTextStyle.current
                            )
                        }, leadingContent = {
                            AsyncImage(
                                model = it.albumImage,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                        }, trailingContent = {
                            IconButton(onClick = {
                                removeFromLocalPlaylist(it.musicId)
                            }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                            }
                        })
                }
            }

        }

    }
}

private val Long.niceTime: String
    get() {
        val second = this / 1000

        val secondString = if (second % 60 >= 10) "${second % 60}" else "0${second % 60}"

        return "${second / 60}:$secondString"
    }


@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
private fun PlayScreenPreview() {
    ComposeMusicTheme {
        PlayScreen(
            currentPlayingSong = QueryDownloadedMusicResult(
                musicId = 0L, name = "暧昧", albumName = "杨丞琳", albumImage = "", path = null
            ), progress = 10000L to 20000, playing = true, emptyList(), {}, {}, {}, {}
        ) {}
    }
}
