package com.ke.compose.music.ui.playlist_top

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.ke.compose.music.db.entity.Playlist
import com.ke.compose.music.ui.component.AppTopBar

@Composable
fun PlaylistTopRoute(
    category: String?,
    onBackButtonClick: () -> Unit,
    onItemClick: (Playlist) -> Unit,
    onCategoryButtonClick: (String?) -> Unit,
) {

    val viewModel: PlaylistTopViewModel = hiltViewModel()

    val list = viewModel.pagingData.collectAsLazyPagingItems()
    viewModel.updateCategory(category)
//    PlaylistTopScreen(
//        list = list,
//        category = category,
//        onCategoryButtonClick = onCategoryButtonClick,
//        onBackButtonClick = onBackButtonClick,
//        onItemClick = onItemClick
//    )
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
//        LazyVerticalGrid(columns = GridCells.Fixed(3), modifier = Modifier.padding(padding)) {
//
//
//        }
        LazyColumn(modifier = Modifier.padding(padding)) {
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
}
