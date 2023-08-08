package com.ke.music.tv.ui.playlist_top

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.tv.material3.Card
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.music.api.response.PlaylistCategory
import com.ke.music.room.db.entity.Playlist
import com.ke.music.tv.ui.components.LocalNavigationHandler
import com.ke.music.tv.ui.components.NavigationAction
import com.ke.music.tv.ui.components.items

@Composable
fun PlaylistTopRoute(
) {

    val viewModel: PlaylistTopViewModel = hiltViewModel()

    val list = viewModel.playlists.collectAsLazyPagingItems()

    val navigationHandler = LocalNavigationHandler.current

    val categoryList by viewModel.categoryList.collectAsStateWithLifecycle()
    PlaylistTopScreen(
        list = list,
        categoryList = categoryList,
        onTabFocus = {

            if (viewModel.selectedCategory == it.name) {
                return@PlaylistTopScreen
            }
            viewModel.selectedCategory = it.name
            list.refresh()
        },
        onItemClick = {
            navigationHandler.navigate(NavigationAction.NavigateToPlaylistDetail(it.id))
        }
    )


}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PlaylistTopScreen(
    list: LazyPagingItems<Playlist>,
    categoryList: List<PlaylistCategory>,
    onTabFocus: (PlaylistCategory) -> Unit,
    onItemClick: (Playlist) -> Unit,
) {

    Column {

//        val tabs = listOf("Tab 1", "Tab 2", "Tab 3")

        if (categoryList.isNotEmpty()) {
            var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

            TabRow(
                selectedTabIndex = selectedTabIndex,
                separator = { Spacer(modifier = Modifier.width(12.dp)) },
                modifier = Modifier
                    .focusRestorer()
                    .padding(16.dp)
            ) {
                categoryList.forEachIndexed { index, category ->
                    key(index) {
                        Tab(
                            selected = index == selectedTabIndex,
                            onFocus = {
                                selectedTabIndex = index
                                onTabFocus(category)
                            },
                        ) {
                            Text(
                                text = category.name,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier.padding(0.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        )
        {
            items(list, key = {
                it.id
            }) {
                Box(modifier = Modifier.padding(8.dp)) {
                    Card(
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1f),
                        onClick = {

                            onItemClick(it!!)
                        }) {

                        Box {
                            AsyncImage(
                                model = it!!.coverImgUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Text(
                                text = "${it.name}\n", maxLines = 2,
                                style = MaterialTheme.typography.bodySmall.copy(
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



