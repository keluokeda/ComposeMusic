package com.ke.compose.music.ui.album.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.LocalBackHandler
import com.ke.compose.music.ui.component.LocalNavigationHandler
import com.ke.compose.music.ui.component.MusicBottomSheetLayout
import com.ke.compose.music.ui.component.MusicView
import com.ke.compose.music.ui.component.NavigationAction
import com.ke.music.download.LocalDownloadManager
import com.ke.music.repository.entity.ShareType
import com.ke.music.room.entity.CommentType
import com.ke.music.room.entity.MusicEntity
import kotlinx.coroutines.launch

@Composable
fun AlbumDetailRoute() {

    val viewModel: AlbumDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AlbumDetailScreen(viewModel.id, uiState = uiState) {
        viewModel.toggleCollect()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun AlbumDetailScreen(id: Long, uiState: AlbumDetailUiState, onCollectClick: () -> Unit) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val navigationHandler = LocalNavigationHandler.current
    var selectedSong by remember {
        mutableStateOf<MusicEntity?>(null)
    }
    MusicBottomSheetLayout(
        selectedSong,
        sheetState = sheetState,
        actions = { }) {
        Scaffold(topBar = {
            AppTopBar(title = { Text(text = "专辑") }, navigationIcon = {
                val backHandler = LocalBackHandler.current
                IconButton(onClick = {
                    backHandler.navigateBack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }, actions = {

                if (uiState.hasData) {
                    IconButton(onClick = onCollectClick) {
                        Icon(
                            imageVector = if (uiState.albumEntity!!.collected) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = null
                        )
                    }
                }

                if (uiState.hasData) {
//                    val appViewModel = LocalAppViewModel.current
                    val downloadManager = LocalDownloadManager.current
                    IconButton(onClick = {
                        downloadManager.downloadAlbum(uiState.albumEntity!!.albumId)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = null
                        )
                    }
                }

                IconButton(onClick = {
                    navigationHandler.navigate(
                        NavigationAction.NavigateToComments(
                            CommentType.Album,
                            id
                        )
                    )
                }) {
                    Icon(imageVector = Icons.Default.Comment, contentDescription = null)
                }

                if (uiState.hasData) {
                    IconButton(onClick = {
                        navigationHandler.navigate(
                            NavigationAction.NavigateToShare(
                                ShareType.Album,
                                uiState.albumEntity!!.albumId,
                                uiState.albumEntity.name,
                                uiState.albumEntity.description ?: "",
                                uiState.albumEntity.image
                            )
                        )
                    }) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null)
                    }
                }


            })
        }) { padding ->


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {

                if (uiState.hasData) {
                    AlbumDetailContent(detail = uiState) {
                        selectedSong = it
                        scope.launch {
                            sheetState.show()
                        }
                    }
                } else {

                    CircularProgressIndicator()
                }
            }
        }
    }
}


@Composable
private fun AlbumDetailContent(
    detail: AlbumDetailUiState,
    onMusicMoreButtonClick: (MusicEntity) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = detail.albumEntity!!.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color.Cyan)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = detail.albumEntity.name, style = MaterialTheme.typography.headlineSmall)

                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = detail.albumEntity.artistName)
                }

                if (detail.albumEntity.description != null)
                    Text(
                        text = detail.albumEntity.description ?: "",
                        style = MaterialTheme.typography.bodySmall
                    )
            }
        }

        items(detail.musicList, key = { it.musicId }) {
            MusicView(
                musicEntity = it,
                rightButton = {
                    IconButton(onClick = {
                        onMusicMoreButtonClick(it)
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


