package com.ke.music.tv.ui.album_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.music.download.LocalDownloadManager
import com.ke.music.tv.IMAGE_SIZE
import com.ke.music.tv.ui.components.LocalNavigationHandler
import com.ke.music.tv.ui.components.NavigationAction
import com.ke.music.tv.ui.components.ShareAction
import com.ke.music.tv.ui.components.SongView
import com.ke.music.viewmodel.AlbumDetailUiState
import com.ke.music.viewmodel.AlbumDetailViewModel

@Composable
fun AlbumDetailRoute() {

    val viewModel: AlbumDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AlbumDetailScreen(uiState = uiState) {
        viewModel.toggleCollect()
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun AlbumDetailScreen(uiState: AlbumDetailUiState, onCollectClick: () -> Unit) {


    val navigationHandler = LocalNavigationHandler.current



    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if (uiState.hasData) {
            Row {
                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .width(IMAGE_SIZE.dp)
                        .padding(vertical = 16.dp)
                ) {

                    Text(
                        text = uiState.albumEntity!!.album.name,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = uiState.albumEntity?.album?.image,
                        contentDescription = null,
                        modifier = Modifier.size(
                            IMAGE_SIZE.dp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {

                        val downloadManager = LocalDownloadManager.current
                        IconButton(onClick = {
                            onCollectClick()
                        }) {
                            Icon(
                                imageVector = if (uiState.albumEntity!!.collected) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = null
                            )
                        }

                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Default.Comment, contentDescription = null)
                        }

                        IconButton(onClick = {
                            navigationHandler.navigate(
                                NavigationAction.NavigateToShare(
                                    ShareAction.Album(
                                        uiState.albumEntity!!
                                    )
                                )
                            )
                        }) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null)
                        }

                        IconButton(onClick = {
                            downloadManager.downloadAlbum(uiState.albumEntity!!.album.albumId)
                        }) {
                            Icon(imageVector = Icons.Default.Download, contentDescription = null)
                        }
                    }


                }

                Spacer(modifier = Modifier.width(16.dp))


                TvLazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(), contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(uiState.songs, key = {
                        it.song.id
                    }) {
                        SongView(index = uiState.songs.indexOf(it), entity = it)
                    }
                }
            }

        } else {

            Text(text = "加载中")
        }
    }

}





