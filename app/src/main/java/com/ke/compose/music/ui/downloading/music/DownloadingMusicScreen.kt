package com.ke.compose.music.ui.downloading.music

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.music.common.entity.DownloadStatus
import com.ke.music.common.entity.ISongEntity
import com.ke.music.viewmodel.DownloadingMusicViewModel

@Composable
fun DownloadingMusicRoute(
    onBackButtonClick: () -> Unit,
) {

    val viewModel: DownloadingMusicViewModel = hiltViewModel()
    val list by viewModel.all.collectAsStateWithLifecycle()

    DownloadingMusicScreen(onBackButtonClick = onBackButtonClick, list = list, {
        viewModel.retry(it)
    }, {
        viewModel.delete(it)
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DownloadingMusicScreen(
    onBackButtonClick: () -> Unit,
    list: List<Pair<ISongEntity, Long>>,
    onRetryButtonClick: (Long) -> Unit,
    deleteButtonClick: (Long) -> Unit,
) {

    Scaffold(
        topBar = {
            AppTopBar(title = {
                Text(text = "下载中的音乐")
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
            items(list, key = {
                it.second
            }) {
                DownloadingMusicView(entity = it.first, retryButtonClick = {
                    onRetryButtonClick(it.second)
                }, deleteButtonClick = {
                    deleteButtonClick(it.second)
                })
            }
        }
    }
}


@Composable
private fun DownloadingMusicView(
    entity: ISongEntity,
    retryButtonClick: () -> Unit = {},
    deleteButtonClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = entity.album.image,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Text(
            text = entity.song.name, modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            maxLines = 1
        )

        if (entity.status == DownloadStatus.Error) {
            IconButton(onClick = retryButtonClick) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            }
        } else {
            IconButton(onClick = {}, enabled = false) {
                Icon(imageVector = Icons.Default.Downloading, contentDescription = null)
            }
        }

        if (entity.status != DownloadStatus.Downloading) {
            IconButton(onClick = deleteButtonClick) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
            }
        }
    }
}
//
//@Composable
//@Preview(showBackground = true)
//private fun DownloadingErrorMusicViewPreview() {
//
//    DownloadingMusicViewPreview(queryDownloadingMusicResult = getDownloadingMusic(Download.STATUS_DOWNLOAD_ERROR))
//}
//
//@Composable
//@Preview(showBackground = true)
//private fun DownloadingDownloadingMusicViewPreview() {
//
//    DownloadingMusicViewPreview(queryDownloadingMusicResult = getDownloadingMusic(Download.STATUS_DOWNLOADING))
//}
//
//private fun getDownloadingMusic(status: Int) = QueryDownloadingMusicResult(
//    0, 0, "漫步人生了", 0, "最爱邓丽君", "", status
//)
//
//@Composable
//private fun DownloadingMusicViewPreview(queryDownloadingMusicResult: QueryDownloadingMusicResult) {
//    ComposeMusicTheme {
//        DownloadingMusicView(entity = queryDownloadingMusicResult)
//    }
//}
