package com.ke.compose.music.ui.play

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.outlined.CommentBank
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.dokar.amlv.LyricsView
import com.dokar.amlv.LyricsViewState
import com.dokar.amlv.parser.LrcLyricsParser
import com.ke.compose.music.ui.theme.ComposeMusicTheme
import com.ke.music.player.service.LocalMusicPlayerController
import com.ke.music.room.entity.DownloadedMusicEntity
import com.ke.music.room.entity.QueryDownloadedMusicResult
import com.ke.music.viewmodel.LocalPlaylistSongsViewModel
import com.ke.music.viewmodel.SongLrcViewModel
import com.orhanobut.logger.Logger


@Composable
fun PlayRoute() {
    val musicPlayerController = LocalMusicPlayerController.current

    val currentPlayingSong by musicPlayerController.currentPlaying.collectAsStateWithLifecycle()
    val playing by musicPlayerController.isPlaying.collectAsStateWithLifecycle()
    val progress by musicPlayerController.progress.collectAsStateWithLifecycle()

    val viewModel: LocalPlaylistSongsViewModel = hiltViewModel()
    val songs by viewModel.songs.collectAsStateWithLifecycle()
    val songLrcViewModel: SongLrcViewModel = hiltViewModel()

    PlayScreen(currentPlayingSong as QueryDownloadedMusicResult?, progress, playing, songs, {
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
    }, getSongLrc =
    {
        songLrcViewModel.getSongLrc(it)
    }, seekTo = {
        musicPlayerController.seekTo(it)
    }
    )
}

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
    removeFromLocalPlaylist: (Long) -> Unit,
    initialType: PlayType = PlayType.Album,
    getSongLrc: suspend (Long) -> String = {
        ""
    },
    seekTo: (Long) -> Unit = {

    }
) {


    var playType by remember {
        mutableStateOf(initialType)
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Crossfade(
                targetState = currentPlayingSong?.albumImage,
                label = "background",
                animationSpec = tween(durationMillis = 1000)
            ) { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .blur(1000.dp),
                    contentScale = ContentScale.Crop,
                )
            }


//            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {

                when (playType) {
                    PlayType.Album -> {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f),
                            verticalArrangement = Arrangement.Center
                        ) {

                            Card {
                                AsyncImage(
                                    model = currentPlayingSong?.albumImage,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .aspectRatio(1f),
//                                    onSuccess = {
//                                        albumBitmap =
//                                            (it.result.drawable as? BitmapDrawable)?.bitmap
//                                    }
                                )
                            }



                            Spacer(modifier = Modifier.height(32.dp))

                            Text(
                                text = currentPlayingSong?.name ?: "",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = currentPlayingSong?.albumName ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )

                        }

                    }

                    PlayType.Lrc -> {
//                    val state =
//                        rememberLyricsViewState(
//                            lrcContent =
//                            "[00:00.000] 作词 : 罗大佑\n[00:01.000] 作曲 : 罗大佑\n[00:14.200]我听到传来的谁的声音\n[00:17.230]像那梦里呜咽中的小河\n[00:21.150]我看到远去的谁的步伐\n[00:24.500]遮住告别时哀伤的眼神\n[00:28.600]不明白的是为何你情愿\n[00:31.710]让风尘刻画你的样子\n[00:35.400]就像早已忘情的世界\n[00:37.900]曾经拥有你的名字我的声音\n[00:42.290]那悲歌总会在梦中惊醒\n[00:45.430]诉说一点哀伤过的往事\n[00:49.120]那看似满不在乎转过身的\n[00:52.300]是风干泪眼后萧瑟的影子\n[00:56.110]不明白的是为何人世间\n[00:59.720]总不能溶解你的样子\n[01:03.590]是否来迟了明日的渊源\n[01:06.800]早谢了你的笑容我的心情\n[01:10.220]不变的你\n[01:13.899]伫立在茫茫的尘世中\n[01:17.690]聪明的孩子\n[01:20.480]提着易碎的灯笼\n[01:24.450]潇洒的你\n[01:27.480]将心事化进尘缘中\n[01:32.390]孤独的孩子\n[01:35.100]你是造物的恩宠\n[01:42.220]\n[02:15.670]我听到传来的谁的声音\n[02:18.840]像那梦里呜咽中的小河\n[02:22.770]我看到远去的谁的步伐\n[02:25.790]遮住告别时哀伤的眼神\n[02:29.700]不明白的是为何你情愿\n[02:33.800]让风尘刻画你的样子\n[02:36.700]就像早已忘情的世界\n[02:39.470]曾经拥有你的名字我的声音\n[02:43.890]那悲歌总会在梦中惊醒\n[02:46.829]诉说一点哀伤过的往事\n[02:50.620]那看似满不在乎转过身的\n[02:54.0]是风干泪眼后萧瑟的影子\n[02:57.510]不明白的是为何人世间\n[03:01.800]总不能溶解你的样子\n[03:04.720]是否来迟了明日的渊源\n[03:07.820]早谢了你的笑容我的心情\n[03:12.0]不变的你\n[03:15.500]伫立在茫茫的尘世中\n[03:18.990]聪明的孩子\n[03:22.400]提着心爱的灯笼\n[03:25.890]潇洒的你\n[03:29.700]将心事化进尘缘中\n[03:32.950]孤独的孩子\n[03:36.140]你是造物的恩宠\n[03:43.530]\n"
//                        )
                        var state by remember {
                            mutableStateOf<LyricsViewState?>(null)
                        }
                        val parser = remember {
                            LrcLyricsParser()
                        }
                        val scope = rememberCoroutineScope()


                        LaunchedEffect(key1 = currentPlayingSong?.musicId) {
                            val songId = currentPlayingSong?.musicId ?: return@LaunchedEffect
                            val lrc = getSongLrc(songId)
                            state = LyricsViewState(
                                parser.parse(lrc), scope
                            )
                        }

//                        LaunchedEffect(key1 = state) {
//
//                            state?.play(progress.first)
//                        }

                        LaunchedEffect(key1 = playing, key2 = state) {
                            Logger.d("播放状态发生变化 $playing , $progress ,$state")
                            if (playing) {
                                state?.play(progress.first)
                            } else {
                                state?.pause()
                            }
                        }

                        if (state != null) {

                            LyricsView(
                                state = state!!,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                darkTheme = true,
                                onLineClick = { s, i ->
//                                    seekTo(s.seekToLine(i))
                                },
                            ) { text, modifier, color, fontSize, fontWeight, lineHeight ->
                                Text(
                                    text = text,
                                    modifier = modifier,
                                    color = color,
                                    fontSize = fontSize,
                                    fontWeight = fontWeight,
                                    lineHeight = lineHeight,
                                )
                            }
                        } else {
                            Text(
                                text = "歌词加载中",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                textAlign = TextAlign.Center
                            )
                        }


                    }

                    PlayType.Songs -> {


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
                                                Color.Red
                                            ) else LocalTextStyle.current
                                        )
                                    }, supportingContent = {
                                        Text(
                                            text = it.albumName,
                                            maxLines = 1,
                                            style = if (isCurrent) LocalTextStyle.current.copy(
                                                Color.Red
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
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = null
                                            )
                                        }
                                    }, colors = ListItemDefaults.colors(
                                        containerColor = Color.Transparent
                                    )
                                )
                            }
                        }

                    }
                }






                Spacer(modifier = Modifier.height(16.dp))

                Column {
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
                        Text(
                            text = progress.first.niceTime,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = progress.second.niceTime,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        val iconSize = 64.dp

                        IconButton(
                            onClick = skipPrevious, modifier = Modifier.size(iconSize)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipPrevious,
                                contentDescription = null,
                                modifier = Modifier.size(iconSize)
                            )
                        }

                        IconButton(
                            onClick = playPause, modifier = Modifier.size(iconSize)
                        ) {
                            Icon(
                                imageVector = if (playing) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(iconSize)
                            )
                        }

                        IconButton(
                            onClick = skipNext, modifier = Modifier.size(iconSize)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SkipNext,
                                contentDescription = null,
                                modifier = Modifier.size(iconSize)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {


                        if (playType == PlayType.Lrc) {
                            OutlinedIconButton(
                                onClick = {
                                    playType = PlayType.Album
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CommentBank,
                                    contentDescription = null,

                                    )
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    playType = PlayType.Lrc
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CommentBank,
                                    contentDescription = null,

                                    )
                            }
                        }


                        if (playType == PlayType.Songs) {
                            OutlinedIconButton(
                                onClick = {
                                    playType = PlayType.Album
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.List,
                                    contentDescription = null,

                                    )
                            }
                        } else {
                            IconButton(
                                onClick = {
                                    playType = PlayType.Songs
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.List,
                                    contentDescription = null,

                                    )
                            }
                        }


                    }
                }


            }
        }

    }
}

private enum class PlayType {
    Album, Lrc, Songs
}

private val Long.niceTime: String
    get() {
        val second = this / 1000

        val secondString = if (second % 60 >= 10) "${second % 60}" else "0${second % 60}"

        return "${second / 60}:$secondString"
    }


@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES)
private fun PlayScreenAlbumPreview() {
    ComposeMusicTheme {
        PlayScreen(
            currentPlayingSong = QueryDownloadedMusicResult(
                musicId = 0L,
                name = "暧昧",
                albumId = 0,
                albumName = "杨丞琳",
                albumImage = "",
                path = null
            ), progress = 10000L to 20000, playing = true, emptyList(), {}, {}, {}, {}, {})
    }
}
