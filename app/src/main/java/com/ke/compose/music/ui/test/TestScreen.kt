package com.ke.compose.music.ui.test

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.compose.music.ui.component.MusicView
import com.ke.music.player.service.LocalMusicPlayerController
import com.ke.music.viewmodel.DownloadedMusicViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestRoute() {
    val musicPlayerController = LocalMusicPlayerController.current

    val isPlaying by musicPlayerController.isPlaying.collectAsStateWithLifecycle()

    val progress by musicPlayerController.progress.collectAsStateWithLifecycle()

    val playingSong by musicPlayerController.currentPlaying.collectAsStateWithLifecycle()

    val current = progress.first
    val total = progress.second

    val viewModel: DownloadedMusicViewModel = hiltViewModel()

    val downloadedSongs by viewModel.downloadedMusicList.collectAsStateWithLifecycle()


    Scaffold { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            LinearProgressIndicator(
                progress = if (total == 0L) 0f else current.toFloat() / total,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)

            )

            ListItem(headlineContent = {
                Text(text = "当前正在播放 $isPlaying , $playingSong")
            }, supportingContent = {
                Text(text = "当前进度 ${progress.first} / ${progress.second}")
            })

            ListItem(headlineContent = {
                Text(text = "播放音乐")
            }, modifier = Modifier.clickable {
                musicPlayerController.playMusic(329925)
            })

            ListItem(headlineContent = {
                Text(text = "播放")
            }, modifier = Modifier.clickable {
                musicPlayerController.play()
            })

            ListItem(headlineContent = {
                Text(text = "暂停")
            }, modifier = Modifier.clickable {
                musicPlayerController.pause()
            })


            ListItem(headlineContent = {
                Text(text = "上一首")
            }, modifier = Modifier.clickable {
                musicPlayerController.skipToPrevious()
            })


            ListItem(headlineContent = {
                Text(text = "下一首")
            }, modifier = Modifier.clickable {
                musicPlayerController.skipToNext()
            })




            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(downloadedSongs, key = {
                    it.musicId
                }) {
                    MusicView(musicEntity = it)
                }
            }
        }
    }
}