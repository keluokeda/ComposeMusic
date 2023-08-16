package com.ke.music.tv.ui.artist_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
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
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.music.room.db.entity.HotArtist
import com.ke.music.tv.ui.components.LocalNavigationHandler
import com.ke.music.tv.ui.components.NavigationAction
import com.ke.music.tv.ui.theme.ComposeMusicTheme

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

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun ArtistListScreen(
    list: LazyPagingItems<HotArtist>,
    area: ArtistArea,
    type: ArtistType,
    onUpdateType: (ArtistType) -> Unit,
    onUpdateArea: (ArtistArea) -> Unit,
) {
//    Scaffold(
//        topBar = {
//            AppTopBar(
//                title = { Text(text = "热门歌手") },
//                navigationIcon = {
//                    IconButton(onClick = {
//                        backHandler.navigateBack()
//                    }) {
//                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
//                    }
//                },
//                actions = {
//
//                    var isAreaContextMenuVisible by rememberSaveable {
//                        mutableStateOf(false)
//                    }
//                    DropDownButton(
//                        content = {
//                            TextButton(onClick = {
//                                isAreaContextMenuVisible = true
//                            }) {
//                                Text(text = area.title)
//                            }
//                        },
//                        isContextMenuVisible = isAreaContextMenuVisible,
//                        onDismissRequest = { isAreaContextMenuVisible = false },
//                        actions = ArtistArea.values().map {
//                            it.title
//                        },
//                        onItemClick = {
//                            isAreaContextMenuVisible = false
//                            onUpdateArea(ArtistArea.values()[it])
//                        }
//                    )
//
//                    var isTypeContextMenuVisible by rememberSaveable {
//                        mutableStateOf(false)
//                    }
//                    DropDownButton(
//                        content = {
//                            TextButton(onClick = {
//                                isTypeContextMenuVisible = true
//                            }) {
//                                Text(text = type.title)
//                            }
//                        },
//                        isContextMenuVisible = isTypeContextMenuVisible,
//                        onDismissRequest = { isTypeContextMenuVisible = false },
//                        actions = ArtistType.values().map {
//                            it.title
//                        },
//                        onItemClick = {
//                            isTypeContextMenuVisible = false
//                            onUpdateType(ArtistType.values()[it])
//                        }
//                    )
//                }
//
//            )
//        },
//
//        ) { paddingValues ->

    Column(
        modifier = Modifier
            .fillMaxSize()
//            .padding(paddingValues)
    ) {

        TabRow(
            title = "类型",
            selected = type.title,
            tabList = ArtistType.values().map { it.title },
            onTabFocus = {
                onUpdateType(ArtistType.values()[it])
            })

        TabRow(
            title = "地区",
            selected = area.title,
            tabList = ArtistArea.values().map { it.title },
            onTabFocus = {
                onUpdateArea(ArtistArea.values()[it])
            })


        LazyVerticalGrid(columns = GridCells.Fixed(5)) {
            items(list, key = {
                it.artistId
            }) {
                val artist = it!!
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(1f)
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {

                    val navigationHandler = LocalNavigationHandler.current

                    Card(onClick = {
                        navigationHandler.navigate(NavigationAction.NavigateToArtistDetail(it.artistId))
                    }) {
                        Box {
                            AsyncImage(
                                model = artist.avatar,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Text(
                                text = artist.name,
                                maxLines = 1,
                                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.BottomCenter)
                                    .background(
                                        Color.Black.copy(alpha = 0.3f)
                                    )
                                    .padding(3.dp)
                            )
                        }
                    }


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

//    }
}

@OptIn(ExperimentalTvMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun TabRow(
    title: String,
    selected: String,
    tabList: List<String>,
    onTabFocus: (Int) -> Unit
) {
//    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }


    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 16.dp)
    ) {

        Text(text = title)


        androidx.tv.material3.TabRow(
            selectedTabIndex = tabList.indexOf(selected),
            separator = { Spacer(modifier = Modifier.width(12.dp)) },
            modifier = Modifier
                .focusRestorer()
                .padding(16.dp)
        ) {


            tabList.forEachIndexed { index, tab ->
                key(index) {
                    Tab(
                        selected = tab == selected,
                        onFocus = {
//                            selectedTabIndex = index
                            onTabFocus(index)
                        },
                    ) {
                        Text(
                            text = tab,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = Devices.TV_1080p)
@Composable
fun TabRowPreview() {
    ComposeMusicTheme {
        TabRow(
            title = "类型",
            "全部",
            tabList = listOf("全部", "男歌手", "女歌手", "乐队"),
            onTabFocus = {})
    }
}


//
//@Composable
//private fun DropDownButton(
//    content: @Composable () -> Unit,
//    isContextMenuVisible: Boolean,
//    onDismissRequest: () -> Unit,
//    actions: List<String>,
//    onItemClick: (Int) -> Unit
//) {
//
//
//    Box {
//        content()
//
//        DropdownMenu(expanded = isContextMenuVisible, onDismissRequest = onDismissRequest) {
//            actions.forEachIndexed { index, s ->
//                DropdownMenuItem(text = { Text(text = s) }, onClick = {
//                    onItemClick(index)
//                })
//
//            }
//        }
//    }
//}

internal enum class ArtistArea(val title: String, val value: Int) {
    All("全部", -1),
    China("华语", 7),
    EA("欧美", 96),
    Japan("日本", 8),
    Korea("韩国", 16),
    Other("其他", 0)
}

internal enum class ArtistType(val title: String, val value: Int) {
    All("全部", -1),
    Man("男歌手", 1),
    Women("女歌手", 2),
    Band("乐队", 3)
}