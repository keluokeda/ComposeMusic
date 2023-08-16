package com.ke.music.watch.presentation.downloaded

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.Scaffold
import com.ke.music.player.service.LocalMusicPlayerController
import com.ke.music.viewmodel.DownloadedMusicViewModel

@Composable
fun DownloadedRoute() {
    val viewModel: DownloadedMusicViewModel = hiltViewModel()

    val list by viewModel.downloadedMusicList.collectAsStateWithLifecycle()

    Scaffold {
        val musicPlayerController = LocalMusicPlayerController.current
//        ScalingLazyColumn {
//            items(list, key = {
//                it.musicId
//            }) {
//                Chip(label = {
//                    Text(text = it.name, maxLines = 1)
//                }, onClick = {
//                    musicPlayerController.playMusic(it.musicId)
//                }, modifier = Modifier.fillMaxWidth())
//            }
//        }
    }
}