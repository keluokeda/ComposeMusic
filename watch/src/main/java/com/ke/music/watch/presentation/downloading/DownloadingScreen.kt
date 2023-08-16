package com.ke.music.watch.presentation.downloading

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.ke.music.viewmodel.DownloadingMusicViewModel

@Composable
fun DownloadingRoute() {
    val viewModel: DownloadingMusicViewModel = hiltViewModel()
    val songs by viewModel.all.collectAsStateWithLifecycle()

    Scaffold {
        ScalingLazyColumn {
            items(songs, key = {
                it.second
            }) {
                Chip(label = {
                    Text(text = it.first.album.name)
                }, icon = {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }, onClick = { viewModel.delete(it.second) }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}