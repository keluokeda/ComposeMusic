package com.ke.compose.music.ui.playlist_top

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.ke.compose.music.db.entity.Playlist
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.items

@Composable
fun PlaylistTopRoute(
    onBackButtonClick: () -> Unit,
    onItemClick: (Playlist) -> Unit,
    onCategoryButtonClick: (String?) -> Unit,
) {

    val viewModel: PlaylistTopViewModel = hiltViewModel()

    val list = viewModel.playlists.collectAsLazyPagingItems()


    PlaylistTopScreen(
        list = list,
        category = viewModel.category,
        onCategoryButtonClick = onCategoryButtonClick,
        onBackButtonClick = onBackButtonClick,
        onItemClick = onItemClick
    )


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun PlaylistTopScreen(
    list: LazyPagingItems<Playlist>,
    category: String?,
    onCategoryButtonClick: (String?) -> Unit,
    onBackButtonClick: () -> Unit,
    onItemClick: (Playlist) -> Unit,
) {
    Scaffold(topBar = {
        AppTopBar(
            title = { Text(text = "网友精选碟") },
            navigationIcon = {
                IconButton(onClick = onBackButtonClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                TextButton(onClick = {
                    onCategoryButtonClick(category)
                }) {
                    Text(text = category ?: "全部")
                }
            }
        )
    }) { padding ->
//        LazyColumn(modifier = Modifier.padding(padding)) {
//            items(
//                count = list.itemCount,
//                key = list.itemKey(key = {
//                    it.id
//                }),
//                contentType = list.itemContentType(
//                )
//            ) { index ->
//                val item = list[index]
//                ListItem(modifier = Modifier.clickable {
//                    onItemClick(item!!)
//                }) {
//                    Text(text = item!!.name)
//                }
//            }
//        }
        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.padding(padding)) {
            items(list, key = {
                it.id
            }) {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .clickable {
                            onItemClick(it!!)
                        }) {
                    AsyncImage(
                        model = it!!.coverImgUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = "${it.name}\n", maxLines = 2, modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.Black.copy(alpha = 0.4f)
                            )
                    )
                }
            }
        }
    }
}
