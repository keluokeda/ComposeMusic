package com.ke.music.watch.presentation.play

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.OutlinedButton
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.ke.music.player.service.LocalMusicPlayerController

@Composable
fun PlayRoute(
    toLocalPlaylist: () -> Unit,
    toMain: () -> Unit
) {
    val musicPlayerController = LocalMusicPlayerController.current

    val playing by musicPlayerController.isPlaying.collectAsStateWithLifecycle()

    val progressPair by musicPlayerController.progress.collectAsStateWithLifecycle()

    val currentSong by musicPlayerController.currentPlaying.collectAsStateWithLifecycle()
    Scaffold {

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(

                modifier = Modifier.fillMaxSize(),
                progress = if (progressPair.second <= 0) 0f else progressPair.first.toFloat() / progressPair.second
            )
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .weight(0.3f)
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = currentSong?.name ?: "",
                        style = MaterialTheme.typography.body1,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = currentSong?.albumName ?: "",

                        style = MaterialTheme.typography.caption2,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                }

                Box(
                    modifier = Modifier

                        .weight(0.4f)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Button(onClick = {
                        if (playing) {
                            musicPlayerController.pause()
                        } else {
                            musicPlayerController.play()
                        }
                    }, modifier = Modifier.align(Alignment.Center)) {
                        Icon(
                            imageVector = if (playing) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            musicPlayerController.skipToPrevious()
                        },
                        modifier = Modifier.align(Alignment.CenterStart)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious, contentDescription = null
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            musicPlayerController.skipToNext()
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext, contentDescription = null
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .weight(0.3f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    OutlinedButton(onClick = toMain) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = null)
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedButton(onClick = toLocalPlaylist) {
                        Icon(imageVector = Icons.Default.List, contentDescription = null)
                    }
                }
            }

//            Button(onClick = {
//                musicPlayerController.playMusic(329925)
//            }) {
//
//                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
//            }
        }
    }
}