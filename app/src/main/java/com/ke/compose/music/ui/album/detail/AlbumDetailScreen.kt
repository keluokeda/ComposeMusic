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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.compose.music.ui.comments.CommentType
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.LocalBackHandler
import com.ke.compose.music.ui.component.LocalNavigationHandler
import com.ke.compose.music.ui.component.NavigationAction
import com.ke.compose.music.ui.component.SongBottomSheetLayout
import com.ke.compose.music.ui.component.SongView
import com.ke.compose.music.ui.share.ShareType
import com.ke.compose.music.ui.theme.ComposeMusicTheme
import com.ke.music.api.response.Song
import kotlinx.coroutines.launch

@Composable
fun AlbumDetailRoute() {

    val viewModel: AlbumDetailViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()



    AlbumDetailScreen(viewModel.id, uiState = uiState, { viewModel.loadDetail() })
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun AlbumDetailScreen(id: Long, uiState: AlbumDetailUiState, retry: () -> Unit) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val navigationHandler = LocalNavigationHandler.current
    var selectedSong by remember {
        mutableStateOf<Song?>(null)
    }
    SongBottomSheetLayout(
        selectedSong = selectedSong,
        sheetState = sheetState,
        actions = { /*TODO*/ }) {
        Scaffold(topBar = {
            AppTopBar(title = { Text(text = "专辑") }, navigationIcon = {
                val backHandler = LocalBackHandler.current
                IconButton(onClick = {
                    backHandler.navigateBack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }, actions = {
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

                if (uiState is AlbumDetailUiState.Detail) {
                    IconButton(onClick = {
                        navigationHandler.navigate(
                            NavigationAction.NavigateToShare(
                                ShareType.Album,
                                uiState.id,
                                uiState.name,
                                uiState.description ?: "",
                                uiState.image
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
                when (uiState) {
                    is AlbumDetailUiState.Detail -> {
                        AlbumDetailContent(detail = uiState) {
                            selectedSong = it
                            scope.launch {
                                sheetState.show()
                            }
                        }
                    }

                    AlbumDetailUiState.Error -> {
                        OutlinedButton(onClick = retry) {
                            Text(text = "错误了，点我重试")
                        }
                    }

                    AlbumDetailUiState.Loading -> {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }

}

@Composable
private fun AlbumDetailContent(
    detail: AlbumDetailUiState.Detail,
    onSongMoreButtonClick: (Song) -> Unit
) {
    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = detail.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .background(Color.Cyan)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(text = detail.name, style = MaterialTheme.typography.headlineSmall)

                TextButton(onClick = { /*TODO*/ }) {
                    Text(text = detail.artistName)
                }

                if (detail.description != null)
                    Text(text = detail.description, style = MaterialTheme.typography.bodySmall)
            }
        }

        items(detail.songs, key = { it.id }) {
            SongView(song = it, onMoreButtonClick = onSongMoreButtonClick)
        }
    }
}


@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun AlbumDetailContentPreview() {
    ComposeMusicTheme {
        AlbumDetailContent(
            detail = AlbumDetailUiState.Detail(
                0,
                "秋天不回来",
                "一个秋天的夜晚，我独自徘徊在城市的老地方，慢慢的发现你已经不在我身旁！记得我们分开的时候你说，明年情人节的时候要回来和我一起过，可是你一走却再无音讯……《秋天不回来》，这首脍炙人口的单曲就是在这样的凄凉的情景下创作出来的，希望能引起您的共鸣！ \\n\\n　2005年12月28日首次参加中国移动彩铃唱作大赛广东省赛区总决赛，获得最佳原创。",
                "王强",
                0,
                "",
                "2022-12-12",
                emptyList()
            )
        ) {

        }
    }
}
