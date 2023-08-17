package com.ke.music.tv.ui.downloading.music

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.music.common.entity.DownloadStatus
import com.ke.music.common.entity.ISongEntity
import com.ke.music.viewmodel.DownloadingMusicViewModel

@Composable
fun DownloadingMusicRoute(
) {

    val viewModel: DownloadingMusicViewModel = hiltViewModel()
    val list by viewModel.all.collectAsStateWithLifecycle()

    DownloadingMusicScreen(list = list, {
        viewModel.retry(it)
    }, {
        viewModel.delete(it)
    })
}

@Composable
private fun DownloadingMusicScreen(
    list: List<Pair<ISongEntity, Long>>,
    onRetryButtonClick: (Long) -> Unit,
    deleteButtonClick: (Long) -> Unit,
) {


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        items(list, key = {
            it.second
        }) {
            DownloadingMusicView(song = it.first, retryButtonClick = {
                onRetryButtonClick(it.second)
            }, deleteButtonClick = {
                deleteButtonClick(it.second)
            })
        }
    }
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun DownloadingMusicView(
    song: ISongEntity,
    retryButtonClick: () -> Unit = {},
    deleteButtonClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = song.album.image,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = song.song.name, modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            maxLines = 1
        )

        if (song.status == DownloadStatus.Error) {
            IconButton(onClick = retryButtonClick) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            }
        } else {
            IconButton(onClick = {}, enabled = false) {
                Icon(imageVector = Icons.Default.Downloading, contentDescription = null)
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (song.status != DownloadStatus.Downloading) {
            IconButton(onClick = deleteButtonClick) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
            }
        }
    }
}


