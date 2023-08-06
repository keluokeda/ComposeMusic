package com.ke.compose.music.ui.recommend_songs

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.compose.music.ui.LocalAppViewModel
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.MusicBottomSheetLayout
import com.ke.compose.music.ui.component.MusicView
import com.ke.music.download.LocalDownloadManager
import com.ke.music.room.entity.MusicEntity
import kotlinx.coroutines.launch

@Composable
fun RecommendSongsRoute(
    onBackButtonTap: () -> Unit,
) {

    val viewModel: RecommendSongsViewModel = hiltViewModel()
    val list by viewModel.songs.collectAsStateWithLifecycle()

    RecommendSongsScreen(list, onBackButtonTap)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun RecommendSongsScreen(
    list: List<MusicEntity>, onBackButtonTap: () -> Unit,
) {
    val scope = rememberCoroutineScope()


    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var selectedMusic by remember {
        mutableStateOf<MusicEntity?>(null)
    }

    val appViewModel = LocalAppViewModel.current

    MusicBottomSheetLayout(
        musicEntity = selectedMusic,
        sheetState = sheetState,
        actions = { }) {

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
                    it.musicId
                }) {
                    MusicView(
                        musicEntity = it,
                        rightButton = {
                            IconButton(onClick = {
                                selectedMusic = it
                                scope.launch {
                                    sheetState.show()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = null
                                )
                            }
                        },
                    )
                }
            }
        }

    }


}