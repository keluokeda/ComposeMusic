package com.ke.compose.music.ui.downloaded.music

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.compose.music.entity.MusicEntity
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.MusicView

@Composable
fun DownloadedMusicRoute(
    onBackButtonClick: () -> Unit,
) {

    val viewModel: DownloadedMusicViewModel = hiltViewModel()
    val list by viewModel.downloadMusicList.collectAsStateWithLifecycle()


    DownloadedMusicScreen(onBackButtonClick = onBackButtonClick, musicList = list) {
        viewModel.deleteDownloadedMusic(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DownloadedMusicScreen(
    onBackButtonClick: () -> Unit,
    musicList: List<MusicEntity>,
    onDeleteButtonClick: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(title = {
                Text(text = "已下载的音乐")
            }, navigationIcon = {
                IconButton(onClick = onBackButtonClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(musicList, key = {
                it.musicId
            }) {
                MusicView(musicEntity = it, rightButton = {
                    IconButton(onClick = {
                        onDeleteButtonClick(it.musicId)
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                })
            }
        }
    }
}