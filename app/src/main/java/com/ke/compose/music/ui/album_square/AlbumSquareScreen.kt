package com.ke.compose.music.ui.album_square

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.ke.compose.music.ui.component.AlbumView
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.LocalBackHandler
import com.ke.compose.music.ui.component.items
import com.ke.music.room.db.entity.Album
import com.ke.music.room.db.entity.NewAlbum
import kotlinx.coroutines.launch

@Composable
fun AlbumSquareRoute() {
    AlbumSquareScreen()
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AlbumSquareScreen() {
    Scaffold(
        topBar = {
            AppTopBar(title = {
                Text(text = "专辑广场")
            }, navigationIcon = {
                val backHandler = LocalBackHandler.current
                IconButton(onClick = {
                    backHandler.navigateBack()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f
            ) {
                5
            }
            val scope = rememberCoroutineScope()

            TabRow(selectedTabIndex = pagerState.currentPage) {
                TabRowItem.values().forEachIndexed { index, tabRowItem ->
                    Tab(selected = pagerState.currentPage == index, onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }, text = {
                        Text(text = tabRowItem.title)
                    })
                }
            }

            HorizontalPager(state = pagerState) {
                Box(modifier = Modifier.fillMaxSize()) {
//                    Text(text = TabRowItem.values()[it].title)

                    when (it) {
                        0 -> AllNewAlbumList()
                        1 -> ZhNewAlbumList()
                        2 -> EaNewAlbumList()
                        3 -> KrNewAlbumList()
                        4 -> JpNewAlbumList()
                        else -> throw IllegalArgumentException("错误的index $it")
                    }
                }
            }
        }

    }
}

@Composable
private fun AllNewAlbumList() {

    val viewModel: AllAlbumSquareViewModel = hiltViewModel()
    val list = viewModel.albumList.collectAsLazyPagingItems()

    NewAlbumList(list)
}

@Composable
private fun ZhNewAlbumList() {

    val viewModel: ZhAlbumSquareViewModel = hiltViewModel()
    val list = viewModel.albumList.collectAsLazyPagingItems()

    NewAlbumList(list)
}

@Composable
private fun EaNewAlbumList() {

    val viewModel: EaAlbumSquareViewModel = hiltViewModel()
    val list = viewModel.albumList.collectAsLazyPagingItems()

    NewAlbumList(list)
}

@Composable
private fun KrNewAlbumList() {

    val viewModel: KrAlbumSquareViewModel = hiltViewModel()
    val list = viewModel.albumList.collectAsLazyPagingItems()

    NewAlbumList(list)
}

@Composable
private fun JpNewAlbumList() {

    val viewModel: JpAlbumSquareViewModel = hiltViewModel()
    val list = viewModel.albumList.collectAsLazyPagingItems()

    NewAlbumList(list)
}


@Composable
private fun NewAlbumList(list: LazyPagingItems<NewAlbum>) {

    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(list, key = {
            it.id
        }) {
            val album = it!!

            AlbumView(
                album = Album(
                    album.albumId, album.name, album.image
                )
            )

        }


    }
}

private enum class TabRowItem(val title: String, val area: String) {
    All("全部", "ALL"),
    Zh("华语", "ZH"),
    Ea("欧美", "EA"),
    Kr("韩国", "KR"),
    Jp("日本", "JP")
}
