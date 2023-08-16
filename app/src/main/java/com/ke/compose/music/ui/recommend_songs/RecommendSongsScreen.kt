package com.ke.compose.music.ui.recommend_songs

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
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
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.SongView
import com.ke.music.common.entity.ISongEntity
import com.ke.music.download.LocalDownloadManager
import com.ke.music.viewmodel.RecommendSongsViewModel

@Composable
fun RecommendSongsRoute(
    onBackButtonTap: () -> Unit,
) {

    val viewModel: RecommendSongsViewModel = hiltViewModel()
    val list by viewModel.songs.collectAsStateWithLifecycle()

    RecommendSongsScreen(list, onBackButtonTap)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecommendSongsScreen(
    list: List<ISongEntity>, onBackButtonTap: () -> Unit,
) {

    Scaffold(topBar = {
        AppTopBar(title = { Text(text = "每日推荐") }, navigationIcon = {
            IconButton(onClick = onBackButtonTap) {
                Icon(Icons.Default.ArrowBack, contentDescription = null)
            }
        }, actions = {

            val downloadManager = LocalDownloadManager.current
            if (list.isNotEmpty())
                IconButton(onClick = {
                    downloadManager.downloadRecommendSongs()
                }) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = null)
                }
        })
    }) { paddingValues ->

        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(list, key = {
                it.song.id
            }) {
                SongView(
                    it,
                )
            }
        }


    }


}