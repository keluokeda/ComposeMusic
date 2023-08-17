package com.ke.music.tv.ui.recommend_songs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.music.common.entity.ISongEntity
import com.ke.music.download.LocalDownloadManager
import com.ke.music.tv.IMAGE_SIZE
import com.ke.music.tv.ui.components.SongView
import com.ke.music.viewmodel.RecommendSongsViewModel

/**
 * 每日推荐
 */
@Composable
fun RecommendSongsRoute(
) {

    val viewModel: RecommendSongsViewModel = hiltViewModel()
    val list by viewModel.songs.collectAsStateWithLifecycle()

    RecommendSongsScreen(list)
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun RecommendSongsScreen(
    list: List<ISongEntity>,
) {

    Row {

        var imageUrl by remember {
            mutableStateOf("")
        }

        Spacer(modifier = Modifier.width(16.dp))


        Column(
            modifier = Modifier
                .width(IMAGE_SIZE.dp)
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(IMAGE_SIZE.dp),
                contentScale = ContentScale.Crop

            )

            Spacer(modifier = Modifier.height(16.dp))

            val downloadManager = LocalDownloadManager.current

            OutlinedButton(onClick = {
                downloadManager.downloadRecommendSongs()
            }) {
                Icon(imageVector = Icons.Default.Download, contentDescription = null)
                Text(text = "全部下载")
            }


        }

        Spacer(modifier = Modifier.width(16.dp))

        TvLazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        ) {
            items(list, key = {
                it.song.id
            }) {
                SongView(
                    list.indexOf(it),
                    entity = it,
                    onHasFocus = { entity ->
                        imageUrl = entity.album.image
                    }

                )
            }
        }
    }


}