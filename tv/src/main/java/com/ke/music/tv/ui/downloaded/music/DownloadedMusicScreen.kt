package com.ke.music.tv.ui.downloaded.music

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import com.ke.music.common.entity.ISongEntity
import com.ke.music.tv.ui.components.SongView
import com.ke.music.viewmodel.DownloadedMusicViewModel

@Composable
fun DownloadedMusicRoute(
) {

    val viewModel: DownloadedMusicViewModel = hiltViewModel()
    val list by viewModel.downloadedMusicList.collectAsStateWithLifecycle()


    DownloadedMusicScreen(songList = list) {
        viewModel.deleteDownloadedMusic(it)
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun DownloadedMusicScreen(
    songList: List<ISongEntity>,
    onDeleteButtonClick: (Long) -> Unit,
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(32.dp)
    ) {
        items(songList, key = {
            it.song.id
        }) {
            SongView(
                songList.indexOf(it),
                entity = it, rightButton = {
                    IconButton(onClick = {
                        onDeleteButtonClick(it.song.id)
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                })
        }
    }

}