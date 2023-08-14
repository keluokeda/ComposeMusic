package com.ke.music.tv.ui.play

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.ListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.dokar.amlv.FadingEdges
import com.dokar.amlv.LyricsView
import com.dokar.amlv.rememberLyricsViewState
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

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }

    val alpha = 0.2f
    val brush = if (bitmap == null) Brush.linearGradient(
        0f to Color.Magenta.copy(alpha),
        0.2f to Color.Gray.copy(alpha),
        0.4f to Color.Blue.copy(alpha),
        0.6f to Color.Yellow.copy(alpha),
        0.8f to Color.White.copy(alpha),
        1f to Color.Cyan.copy(alpha),
    ) else ShaderBrush(
        ImageShader(bitmap!!.asImageBitmap())
    )



    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush
            )

    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Surface(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .aspectRatio(1f),
                tonalElevation = 4.dp,
            ) {
                AsyncImage(
                    model = currentPlayingSong?.albumImage,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    onSuccess = {
//                        bitmap = (it.result.drawable as? BitmapDrawable)?.bitmap
                    }
                )
            }

            Text(
                text = currentPlayingSong?.name ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(text = currentPlayingSong?.albumName ?: "")


            val iconSize = 48.dp

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = {
                        skipPrevious()
                    }, modifier = Modifier.size(iconSize)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = null,

                        )
                }

                IconButton(
                    onClick = {
                        playPause()
                    }, modifier = Modifier.size(iconSize)
                ) {
                    Icon(
                        imageVector = if (playing) Icons.Default.Pause else
                            Icons.Default.PlayArrow,
                        contentDescription = null,

                        )
                }

                IconButton(
                    onClick = {
                        skipNext()
                    }, modifier = Modifier.size(iconSize)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = null,

                        )
                }
            }
            Spacer(modifier = Modifier.weight(1f))


        }

//        TvLazyColumn(
//            modifier = Modifier
//                .weight(1f)
//                .fillMaxHeight()
//                .background(Color.Black)
//        ) {
//
//
//        }

        val state = rememberLyricsViewState(lrcContent = LRC_HELP)

        LaunchedEffect(key1 = playing) {
            if (playing) {
                state.play(progress.first)
            } else {
                state.pause()
            }
        }

        LyricsView(
            state = state,
            modifier = Modifier.weight(weight = 1f),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 150.dp,
            ),
            darkTheme = false,
            fadingEdges = FadingEdges(top = 16.dp, bottom = 150.dp),
        ) { text: String, modifier: Modifier,
            color: Color,
            fontSize: TextUnit, fontWeight: FontWeight, lineHeight: TextUnit ->

            Text(
                text = text,
                modifier = modifier,
                color = color,
                fontSize = fontSize,
                fontWeight = fontWeight,
                lineHeight = lineHeight,
            )

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
                        Icon(
                            imageVector = Icons.Default.PlayCircleOutline,
                            contentDescription = null
                        )
                    })
            }
        }

    }
}

private val LRC_HELP =
    "[00:00.00] 作词 : 王中言\n[00:01.00] 作曲 : 崔浚荣\n[00:16.72]孙：梦中人 熟悉的脸孔\n[00:24.06]你是我守候的温柔\n[00:31.38]就算泪水淹没天地 我不会放手\n[00:46.14]每一刻 孤独的承受\n[00:53.53]只因我曾许下承诺\n[01:00.85]合：你我之间熟悉的感动\n[01:08.36]爱就要苏醒\n[01:14.75]韩：万世沧桑唯有爱是 永远的神话\n[01:22.07]潮起潮落始终不悔 真爱的相约\n[01:29.46]几番苦痛的纠缠 多少黑夜挣扎\n[01:36.93]紧握双手让我和你 再也不离分\n[01:59.84]孙：枕上雪 冰封的爱恋\n[02:07.33]真心相拥才能融解\n[02:14.66]合：风中摇曳炉上的火 不灭亦不休\n[02:29.50]等待花开春去春又来\n[02:37.00]无情岁月笑我痴狂\n[02:44.28]心如钢铁任世界荒芜\n[02:51.63]思念永相随\n[02:58.08]孙：万世沧桑唯有爱是 永远的神话\n[03:05.39]潮起潮落始终不悔 真爱的相约\n[03:12.87]几番苦痛的纠缠 多少黑夜挣扎\n[03:20.32]紧握双手让我和你 再也不离分\n[03:27.69]合：悲欢岁月唯有爱是 永远的神话\n[03:34.98]谁都没有遗忘古老 古老的誓言\n[03:42.51]你的泪水化为漫天 飞舞的彩蝶\n[03:49.85]爱是翼下之风两心 相随自在飞\n[03:57.22]悲欢岁月唯有爱是 永远的神话\n[04:04.54]谁都没有遗忘古老 古老的誓言\n[04:11.85]你的泪水化为漫天 飞舞的彩蝶\n[04:19.40]爱是翼下之风两心 相随自在飞\n[04:29.48]韩：你是我心中唯一 美丽的神话\n"
//    "[00:00.000] 作词 : 李克勤\n[00:01.000] 作曲 : 中島みゆき\n[00:02.000] 编曲 : 卢东尼\n[00:05.457]\n[00:13.446]天空一片蔚蓝\n[00:15.711]清风添上了浪漫\n[00:18.146]心里那份柔情蜜意似海 无限\n[00:31.924]在那遥远有意无意遇上\n[00:35.329]共你初次邂逅谁没有遐想\n[00:39.924]诗一般的落霞\n[00:42.003]酒一般的夕阳\n[00:44.128]似是月老给你我留印象\n[00:48.534]斜阳离去朗月已换上\n[00:52.557]没法掩盖这份情欲盖弥彰\n[00:56.730]这一刹 情一缕 影一对 人一双\n[01:01.226]那怕热炽爱一场\n[01:05.294]潮汐退和涨\n[01:07.034]月冷风和霜\n[01:09.136]夜雨的狂想\n[01:11.285]野花的微香\n[01:13.498]伴我星夜里幻想\n[01:17.617]方知不用太紧张\n[01:24.019]没法隐藏这份爱\n[01:28.189]是我深情深似海\n[01:32.674]一生一世难分开难改变也难再\n[01:36.388]让你的爱满心内\n[02:03.131]在那遥远有意无意遇上\n[02:06.517]共你初次邂逅谁没有遐想\n[02:11.101]诗一般的落霞\n[02:12.995]酒一般的夕阳\n[02:15.253]似是月老给你我留印象\n[02:19.686]斜阳离去朗月已换上\n[02:23.469]没法掩盖这份情欲盖弥彰\n[02:27.970]这一刹 情一缕 影一对 人一双\n[02:32.339]那怕热炽爱一场\n[02:36.262]潮汐退和涨\n[02:38.225]月冷风和霜\n[02:40.392]夜雨的狂想\n[02:42.544]野花的微香\n[02:44.779]伴我星夜里幻想\n[02:48.948]方知不用太紧张\n[02:55.286]没法隐藏这份爱\n[02:59.568]是我深情深似海\n[03:03.849]一生一世难分开难改变也难再\n[03:07.786]让你的爱满心内\n[03:12.391]潮汐退和涨\n[03:14.408]月冷风和霜\n[03:16.501]夜雨的狂想\n[03:18.627]野花的微香\n[03:20.818]伴我星夜里幻想\n[03:24.999]方知不用太紧张\n[03:31.449]没法隐藏这份爱\n[03:35.601]是我深情深似海\n[03:39.880]一生一世难分开难改变也难再\n[03:43.497]让你的爱满心内\n[03:48.255]让我的爱全给你全给我最爱\n[03:52.406]地老天荒仍未改\n[04:00.482]\n[04:20.568]监制 : 陈永明\n[04:21.954]Publisher : Yamaha Ms Foundation (Golden Pony)\n"
//
//
//private const val LRC_HELP = """
//[ar:The Beatles]
//[ti:Help!]
//[al:Help!]
//[length:02:19.23]
//
//[00:01.00]Help! I need somebody
//[00:02.91]Help! Not just anybody
//[00:05.16]Help! You know I need someone
//[00:07.91]Help!
//[00:10.66](When) When I was younger (When I was young) so much younger than today
//[00:15.66](I never need) I never needed anybody's help in any way
//[00:20.76](Now) But now these days are gone (These days are gone) and I'm not so self assured
//[00:25.43](And now I find) Now I find I've changed my mind, I've opened up the doors
//[00:30.42]Help me if you can, I'm feeling down
//[00:34.94]And I do appreciate you being 'round
//[00:40.74]Help me get my feet back on the ground
//[00:44.94]Won't you please, please help me?
//[00:50.94](Now) And now my life has changed (My life has changed) in oh so many ways
//[00:56.20](My independence) My independence seems to vanish in the haze
//[01:01.21](But) But ev'ry now (Every now and then) and then I feel so insecure
//[01:05.92](I know that I) I know that I just need you like I've never done before
//[01:11.00]Help me if you can, I'm feeling down
//[01:15.43]And I do appreciate you being 'round
//[01:20.47]Help me get my feet back on the ground
//[01:25.64]Won't you please, please help me?
//[01:31.43]When I was younger, so much younger than today
//[01:36.65]I never needed anybody's help in any way
//[01:41.39](Now) But now these days are gone (These days are gone) and I'm not so self assured
//[01:46.17](And now I find) Now I find I've changed my mind, I've opened up the doors
//[01:51.38]Help me if you can, I'm feeling down
//[01:56.17]And I do appreciate you being 'round
//[02:01.36]Help me get my feet back on the ground
//[02:06.08]Won't you please, please help me?
//[02:10.12]Help me, help me
//[02:12.85]Ooh
//"""


@Composable
@Preview(device = Devices.TV_1080p)
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