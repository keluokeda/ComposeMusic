package com.ke.music.watch.presentation.my_playlist

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.ke.music.common.entity.IPlaylist
import com.ke.music.viewmodel.PlaylistViewModel

@Composable
fun MyPlaylistRoute(
    onItemClick: (IPlaylist) -> Unit,
) {
    val viewModel: PlaylistViewModel = hiltViewModel()

    val list by viewModel.playlistList.collectAsStateWithLifecycle()

    Scaffold {
        ScalingLazyColumn {
            items(list, key = {
                it.id
            }) {
                Chip(label = {
                    Text(text = it.name, maxLines = 1)
                }, onClick = {
                    onItemClick(it)
                }, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}