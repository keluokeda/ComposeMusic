package com.ke.music.tv.ui.album_square

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.music.common.entity.IAlbum
import com.ke.music.tv.ui.components.LocalNavigationHandler
import com.ke.music.tv.ui.components.NavigationAction
import com.ke.music.tv.ui.components.items


@Composable
fun AlbumSquareRoute() {
    val viewModel: AlbumSquareViewModel = hiltViewModel()

    val list = viewModel.albumList.collectAsLazyPagingItems()

    AlbumSquareScreen(list, onTabFocus = {
        if (viewModel.area.value == it) {
            return@AlbumSquareScreen
        }
        viewModel.updateArea(it)
        list.refresh()
    })
}


@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AlbumSquareScreen(
    list: LazyPagingItems<IAlbum>,
    onTabFocus: (String) -> Unit,
) {

    Column {

        var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

        TabRow(
            selectedTabIndex = selectedTabIndex,
            separator = { Spacer(modifier = Modifier.width(12.dp)) },
            modifier = Modifier
                .focusRestorer()
                .padding(16.dp)
        ) {
            TabRowItem.values().forEachIndexed { index, item ->
                key(index) {
                    Tab(
                        selected = index == selectedTabIndex,
                        onFocus = {
                            selectedTabIndex = index
                            onTabFocus(item.area)
                        },
                    ) {
                        Text(
                            text = item.title,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        LazyVerticalGrid(columns = GridCells.Fixed(5)) {


            items(list, key = {
                it.albumId
            }) {
                val item = it!!
                val navigationHandler = LocalNavigationHandler.current
                Box(modifier = Modifier.padding(16.dp)) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1f),
                        onClick = {
                            navigationHandler.navigate(NavigationAction.NavigateToAlbumDetail(it.albumId))
                        }) {

                        Box {
                            AsyncImage(
                                model = item.image,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Text(
                                text = it.name, maxLines = 1,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        Color.Black.copy(alpha = 0.4f)
                                    )
                                    .padding(4.dp)

                            )
                        }

                    }
                }
            }
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
