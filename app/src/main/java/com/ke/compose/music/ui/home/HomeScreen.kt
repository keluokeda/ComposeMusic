package com.ke.compose.music.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.LocalNavigationHandler
import com.ke.compose.music.ui.component.NavigationAction
import com.ke.compose.music.ui.main.MainScreenFragment
import com.ke.compose.music.ui.theme.ComposeMusicTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen() {
    Scaffold(topBar = {
        AppTopBar(title = {
            Text(text = MainScreenFragment.Home.label)
        },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                }
            }
        )
    }) {
        Column(Modifier.padding(it)) {
            val navigationHandler = LocalNavigationHandler.current
            ListItem(modifier = Modifier.clickable {
                navigationHandler.navigate(NavigationAction.NavigateToPlaylistTop)
            }) {
                Text(text = "网友精选碟")
            }

            ListItem(modifier = Modifier.clickable {
                navigationHandler.navigate(NavigationAction.NavigateToHighqualityPlaylist)
            }) {
                Text(text = "精品歌单")
            }

            ListItem(modifier = Modifier.clickable {
                navigationHandler.navigate(NavigationAction.NavigateToDownloadedMusic)
            }) {
                Text(text = "已下载的音乐")
            }

            ListItem(modifier = Modifier.clickable {
                navigationHandler.navigate(NavigationAction.NavigateToDownloadingMusic)
            }) {
                Text(text = "下载中的音乐")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    ComposeMusicTheme {
        HomeScreen()
    }
}