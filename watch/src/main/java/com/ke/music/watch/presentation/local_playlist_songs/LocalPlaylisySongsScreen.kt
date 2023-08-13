package com.ke.music.watch.presentation.local_playlist_songs

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
import com.ke.music.viewmodel.LocalPlaylistSongsViewModel


@Composable
fun LocalPlaylistSongsRoute() {
    val viewModel: LocalPlaylistSongsViewModel = hiltViewModel()

    val songs by viewModel.songs.collectAsStateWithLifecycle()

    Scaffold {
        ScalingLazyColumn {
            items(songs, key = {
                it.musicId
            }) {
//                SwipeToDismissBox(onDismissed = {
//
//                }) { _ ->
//                    Text(text = it.name)
//                }
                Chip(label = {
                    Text(text = it.name)
                }, icon = {
                    Icon(
                        imageVector = Icons.Default.Clear, contentDescription = null
                    )
                }, onClick = {
                    viewModel.removeSong(it.musicId)
                }, modifier = Modifier.fillMaxWidth())
            }
        }

    }


}