package com.ke.music.watch.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text

@Composable
fun MainRoute(
    toUserPlaylist: () -> Unit,
    toDownloaded: () -> Unit,
    toPlay: () -> Unit
) {
    Scaffold {

        ScalingLazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Chip(label = {
                    Text(text = "我的歌单")
                }, onClick = {
                    toUserPlaylist()
                }, modifier = Modifier.fillMaxWidth()
                )


            }
            item {
                Chip(
                    label = {
                        Text(text = "我的下载")
                    }, onClick = toDownloaded, modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Chip(label = {
                    Text(text = "正在下载")
                }, onClick = { }, modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Chip(
                    label = {
                        Text(text = "播放")
                    }, onClick = toPlay, modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}