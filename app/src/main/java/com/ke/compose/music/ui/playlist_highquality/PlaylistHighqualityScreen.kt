package com.ke.compose.music.ui.playlist_highquality

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import com.ke.compose.music.db.entity.Playlist
import com.ke.compose.music.db.entity.PlaylistTag
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.theme.ComposeMusicTheme
import kotlinx.coroutines.launch

@Composable
fun PlaylistHighqualityRoute(
    onBackButtonClick: () -> Unit,
    onItemClick: (Playlist) -> Unit,

    ) {
    val viewModel: PlaylistHighqualityViewModel = hiltViewModel()
    val tags by viewModel.tags.collectAsStateWithLifecycle()

    PlaylistHighqualityScreen(tags = tags, onBackButtonClick = onBackButtonClick, onItemClick)
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
private fun PlaylistHighqualityScreen(
    tags: List<PlaylistTag>,
    onBackButtonClick: () -> Unit,
    onItemClick: (Playlist) -> Unit,

    ) {
    Scaffold(topBar = {
        AppTopBar(
            navigationIcon = {
                IconButton(onClick = onBackButtonClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            title = {
                Text(text = "精品歌单")
            }
        )
    }) { padding ->


        Column(modifier = Modifier.padding(padding)) {
            if (tags.isNotEmpty()) {

//                var selectedTabIndex by remember {
//                    mutableStateOf(0)
//                }

                val pagerState = rememberPagerState(initialPage = 0)


                val scope = rememberCoroutineScope()


                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 0.dp,
                    divider = {
                    }
                ) {
                    tags.forEachIndexed { index, playlistTag ->
                        Tab(selected = index == pagerState.currentPage, onClick = {
//                            pagerState.currentPage = index
//                            pagerState.
                            scope.launch {
                                pagerState.scrollToPage(index)
                            }
                        }) {
                            Text(
                                text = playlistTag.name,
                                modifier = Modifier.padding(vertical = 16.dp)
                            )
                        }
                    }
                }

                HorizontalPager(
                    pageCount = tags.size,
                    state = pagerState,
                    key = {
                        it
                    }
                ) {
                    PlaylistContainerScreen(tag = tags[it].name, onItemClick)
                }
            }


        }
    }
}


@Composable
@Preview(showSystemUi = true, showBackground = true)
private fun PlaylistHighqualityScreenPreview() {
    ComposeMusicTheme {
        PlaylistHighqualityScreen(
            tags = listOf(
                PlaylistTag(tagId = 0, name = "国语", hot = false, index = 0),
                PlaylistTag(tagId = 0, name = "日本", hot = false, index = 0),
                PlaylistTag(tagId = 0, name = "韩国", hot = false, index = 0),
                PlaylistTag(tagId = 0, name = "美国", hot = false, index = 0),
                PlaylistTag(tagId = 0, name = "越南", hot = false, index = 0),

                ), {}
        ) {

        }
    }
}


@Composable
private fun PlaylistContainerScreen(
    tag: String,
    onItemClick: (Playlist) -> Unit,
) {

//    val viewModel: HighQualityPlaylistListViewModel = hiltViewModel()
//    viewModel.tag = tag
//    val list = viewModel.pagingData.collectAsLazyPagingItems()
//    PlaylistListScreen(list, onItemClick)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun PlaylistListScreen(
    list: LazyPagingItems<Playlist>, onItemClick: (Playlist) -> Unit,
) {
    LazyColumn {
        items(list, key = {
            it.id
        }) {
            ListItem(modifier = Modifier.clickable {
                onItemClick(it!!)
            }) {
                Text(text = it!!.name)
            }
        }
    }
}