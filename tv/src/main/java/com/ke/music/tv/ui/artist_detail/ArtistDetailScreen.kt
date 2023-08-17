package com.ke.music.tv.ui.artist_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import com.ke.music.common.entity.ISongEntity
import com.ke.music.tv.ui.components.SongView
import com.orhanobut.logger.Logger


@OptIn(ExperimentalTvMaterial3Api::class)
private val navigationRow: @Composable (
    drawerValue: DrawerValue,
    menu: MenuItem,
    modifier: Modifier,
    onMenuSelected: ((menuItem: MenuItem) -> Unit),
) -> Unit =
    { drawerValue, menu, modifier, onMenuSelected ->
//        val padding = animateDpAsState(
//            animationSpec = keyframes {
//                this.delayMillis = 100
//            },
//            targetValue = if (drawerValue == DrawerValue.Open) 4.dp else 0.dp, label = "",
//        )

        val focusRequester = FocusRequester()

        LaunchedEffect(key1 = Unit) {
            if (menu == MenuItem.HotSongs) {
                focusRequester.requestFocus()
            }
        }

        Surface(
            onClick = {
//                onMenuSelected.invoke(menu)
            },

            modifier =
            modifier
                .padding(vertical = 4.dp)
                .onFocusChanged {
                    if (it.hasFocus) {
                        onMenuSelected(menu)
                    }
                }
                .focusRequester(focusRequester)
//                .focusable()

//                .then(if (drawerValue == DrawerValue.Open) modifier.width(170.dp) else modifier)
        ) {
            Row(
                Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                menu.icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = menu.text,
                        modifier = Modifier
                    )
//                    Spacer(modifier = Modifier.padding(horizontal = padding.value))
                }
//                AnimatedVisibility(
//                    visible = drawerValue == DrawerValue.Open,
//                    modifier = Modifier.height(20.dp)
//                ) {
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = menu.text,
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium
                )
//                }
            }
        }
    }

private enum class MenuItem(
    val text: String,
    val icon: ImageVector? = null,
) {
    HotSongs("热门歌曲", Icons.Default.MusicNote),
    AllAlbum("所有专辑", Icons.Default.Album),
    Mv("相关MV", Icons.Default.VideoLibrary)
}


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ArtistDetailRoute() {
    var selectedItem by remember {
        mutableStateOf(MenuItem.HotSongs)
    }

    val viewModel: ArtistHotSongsViewModel = hiltViewModel()

    val list by viewModel.hotSongs.collectAsStateWithLifecycle()

    NavigationDrawer(
        drawerContent = { drawerValue ->
            Column(
                Modifier
                    .background(Color.Gray)
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                MenuItem.values().forEach { menuItem ->
                    navigationRow(drawerValue, menuItem, Modifier) {
                        Logger.d("onMenuSelected $it")

                        selectedItem = it
                    }


                }
            }
        },
        drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    ) {
//        ArtistDescScreen()

        when (selectedItem) {
            MenuItem.HotSongs -> {
                ArtistHotSongs(list = list)
            }

            MenuItem.AllAlbum -> {
//                ArtistDescScreen()
            }

            MenuItem.Mv -> {
//                ArtistDescScreen()

            }
        }
    }

}
//
//@OptIn(ExperimentalTvMaterial3Api::class)
//@Composable
//private fun ArtistDescScreen() {
//    val viewModel: ArtistDescViewModel = hiltViewModel()
//    val descriptions by viewModel.artistDescriptionList.collectAsStateWithLifecycle()
//
//
//    TvLazyColumn {
//        items(descriptions, key = {
//            it.id
//        }) {
//            Column {
//                Text(text = it.title)
//                Text(text = it.content)
//            }
//        }
//    }
//}


@Composable
fun ArtistHotSongs(list: List<ISongEntity>) {

    TvLazyColumn {
        items(list, key = {
            it.song.id
        }) {
            SongView(index = list.indexOf(it), entity = it, autoRequestFocus = false)
        }
    }
}
