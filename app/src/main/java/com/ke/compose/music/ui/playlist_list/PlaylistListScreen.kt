package com.ke.compose.music.ui.playlist_list

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.compose.music.observeWithLifecycle
import com.ke.compose.music.toast
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.LocalBackHandler
import com.ke.music.common.entity.IPlaylist
import com.ke.music.viewmodel.PlaylistListViewModel

@Composable
fun PlaylistListRoute(
    onNewButtonClick: () -> Unit,
) {
    val viewModel: PlaylistListViewModel = hiltViewModel()
    val list by viewModel.list.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val context = LocalContext.current.applicationContext
    viewModel.result.observeWithLifecycle {
        if (it) {
            dispatcher?.onBackPressed()
            context.toast("收藏成功")
        } else {
            context.toast("收藏失败")
        }
    }

    PlaylistListScreen(
        list = list,
        loading = loading,
        onSelected = {
            viewModel.collectSongToPlaylist(it.id)
        },
        onNewButtonClick = onNewButtonClick
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun PlaylistListScreen(
    list: List<IPlaylist>,
    loading: Boolean,
    onSelected: (IPlaylist) -> Unit,
    onNewButtonClick: () -> Unit,
) {
    Scaffold(topBar = {
        AppTopBar(
            title = { Text(text = "选择歌单") },
            actions = {
                IconButton(onClick = onNewButtonClick) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                }
            }, navigationIcon = {

                val backHandler = LocalBackHandler.current

                IconButton(onClick = {
                    backHandler.navigateBack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
    }) { paddingValues ->

        if (loading) {

            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {


            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(list, key = {
                    it.id
                }) {
                    ListItem(icon = {
                        AsyncImage(
                            model = it.coverImgUrl,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }, modifier = Modifier.clickable {
                        onSelected(it)
                    }) {
                        Text(text = it.name)
                    }
                }
            }
        }


    }
}
