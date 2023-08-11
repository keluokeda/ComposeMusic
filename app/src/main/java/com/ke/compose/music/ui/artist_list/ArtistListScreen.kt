package com.ke.compose.music.ui.artist_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.LocalBackHandler
import com.ke.compose.music.ui.component.LocalNavigationHandler
import com.ke.compose.music.ui.component.NavigationAction
import com.ke.compose.music.ui.component.items
import com.ke.music.room.db.entity.HotArtist
import com.ke.music.viewmodel.ArtistArea
import com.ke.music.viewmodel.ArtistListViewModel
import com.ke.music.viewmodel.ArtistType

@Composable
fun ArtistListRoute() {
    val viewModel: ArtistListViewModel = hiltViewModel()

    val list = viewModel.artistList.collectAsLazyPagingItems()

    val area by viewModel.area.collectAsStateWithLifecycle()
    val type by viewModel.type.collectAsStateWithLifecycle()

    ArtistListScreen(list, area, type, {
        viewModel.updateType(it)
        list.refresh()
    }, {
        viewModel.updateArea(it)
        list.refresh()
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ArtistListScreen(
    list: LazyPagingItems<HotArtist>,
    area: ArtistArea,
    type: ArtistType,
    onUpdateType: (ArtistType) -> Unit,
    onUpdateArea: (ArtistArea) -> Unit,
) {
    val backHandler = LocalBackHandler.current
    Scaffold(
        topBar = {
            AppTopBar(
                title = { Text(text = "热门歌手") },
                navigationIcon = {
                    IconButton(onClick = {
                        backHandler.navigateBack()
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {

                    var isAreaContextMenuVisible by rememberSaveable {
                        mutableStateOf(false)
                    }
                    DropDownButton(
                        content = {
                            TextButton(onClick = {
                                isAreaContextMenuVisible = true
                            }) {
                                Text(text = area.title)
                            }
                        },
                        isContextMenuVisible = isAreaContextMenuVisible,
                        onDismissRequest = { isAreaContextMenuVisible = false },
                        actions = ArtistArea.values().map {
                            it.title
                        },
                        onItemClick = {
                            isAreaContextMenuVisible = false
                            onUpdateArea(ArtistArea.values()[it])
                        }
                    )

                    var isTypeContextMenuVisible by rememberSaveable {
                        mutableStateOf(false)
                    }
                    DropDownButton(
                        content = {
                            TextButton(onClick = {
                                isTypeContextMenuVisible = true
                            }) {
                                Text(text = type.title)
                            }
                        },
                        isContextMenuVisible = isTypeContextMenuVisible,
                        onDismissRequest = { isTypeContextMenuVisible = false },
                        actions = ArtistType.values().map {
                            it.title
                        },
                        onItemClick = {
                            isTypeContextMenuVisible = false
                            onUpdateType(ArtistType.values()[it])
                        }
                    )
                }

            )
        },

        ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            val navigationHandler = LocalNavigationHandler.current

            LazyVerticalGrid(columns = GridCells.Fixed(3)) {
                items(list, key = {
                    it.id
                }) {
                    val artist = it!!
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .aspectRatio(1f)
                            .clickable {
                                navigationHandler.navigate(
                                    NavigationAction.NavigateToArtistDetail(
                                        artist.artistId
                                    )
                                )
                            },
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        AsyncImage(
                            model = artist.avatar,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        Text(
                            text = artist.name,
                            maxLines = 1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color.Black.copy(alpha = 0.3f)
                                )
                                .padding(3.dp)
                        )
                    }
                }
            }

//            LazyColumn {
//                items(
//                    count = list.itemCount,
//                    key = list.itemKey(key = {
//                        it.id
//                    }),
//                    contentType = list.itemContentType(
//                    )
//                ) { index ->
//                    val item = list[index]!!
//                    ListItem(headlineText = {
//                        Text(text = item.name)
//                    })
//                }
//            }
        }

    }
}


@Composable
private fun DropDownButton(
    content: @Composable () -> Unit,
    isContextMenuVisible: Boolean,
    onDismissRequest: () -> Unit,
    actions: List<String>,
    onItemClick: (Int) -> Unit
) {


    Box {
        content()

        DropdownMenu(expanded = isContextMenuVisible, onDismissRequest = onDismissRequest) {
            actions.forEachIndexed { index, s ->
                DropdownMenuItem(text = { Text(text = s) }, onClick = {
                    onItemClick(index)
                })

            }
        }
    }
}

