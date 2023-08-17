package com.ke.music.tv.ui.my_playlist

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.foundation.lazy.grid.items
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.music.common.entity.IPlaylist
import com.ke.music.common.observeWithLifecycle
import com.ke.music.repository.toast
import com.ke.music.viewmodel.PlaylistListViewModel

@Composable
fun MyPlaylistRoute(
) {
    val viewModel: PlaylistListViewModel = hiltViewModel()
    val list by viewModel.list.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val backHandler = LocalOnBackPressedDispatcherOwner.current
    viewModel.result.observeWithLifecycle {
        if (it) {
            context.toast("收藏成功")
            backHandler?.onBackPressedDispatcher?.onBackPressed()
        } else {
            context.toast("收藏失败")
        }
    }
    MyPlaylistScreen(
        list = list,
        loading,
        onSelected = {
            viewModel.collectSongToPlaylist(it.id)
        },
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun MyPlaylistScreen(
    list: List<IPlaylist>,
    loading: Boolean,
    onSelected: (IPlaylist) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        TvLazyVerticalGrid(
            columns = TvGridCells.Fixed(4),
            modifier = Modifier.fillMaxSize()
        ) {
            items(list, key = {
                it.id
            }) {
                Box(modifier = Modifier.padding(16.dp)) {
                    Card(onClick = {
                        onSelected(it)
                    }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            AsyncImage(
                                model = it.coverImgUrl,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )

                            Text(
                                text = it.name,
                                maxLines = 1,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color.Black.copy(
                                            alpha = 0.3f
                                        )
                                    )
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }
        }

        if (loading) {
            Text(
                text = "提交中", modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = Color.Black.copy(alpha = .3f)
                    ),
                textAlign = TextAlign.Center
            )
        }
    }


}
