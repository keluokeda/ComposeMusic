package com.ke.music.tv.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.ListItem
import androidx.tv.material3.Text
import com.ke.music.tv.ui.components.LocalNavigationHandler
import com.ke.music.tv.ui.components.NavigationAction

@Composable
fun MainRoute() {
    MainScreen()
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(device = Devices.TV_1080p)
@Composable
private fun MainScreen() {
    Column {
        val navigationHandler = LocalNavigationHandler.current


        ListItem(
            selected = false,
            onClick = {
                navigationHandler.navigate(NavigationAction.NavigateToRecommendSongs)
            },
            headlineContent = { Text(text = "每日推荐") })

        ListItem(
            selected = false,
            onClick = {
                navigationHandler.navigate(NavigationAction.NavigateToDownloadedMusic)
            },
            headlineContent = { Text(text = "我的下载") })

        ListItem(
            selected = false,
            onClick = {
                navigationHandler.navigate(NavigationAction.NavigateToDownloadingMusic)
            },
            headlineContent = { Text(text = "正在下载") })

//        ListItem(
//            selected = false,
//            onClick = {
//                navigationHandler.navigate(NavigationAction.NavigateToPlaylistTop())
//            },
//            headlineContent = { Text(text = "网友精选碟") })

        ListItem(
            selected = false,
            onClick = {
                navigationHandler.navigate(NavigationAction.NavigateToUserPlaylist)
            },
            headlineContent = { Text(text = "我的歌单") })

        ListItem(
            selected = false,
            onClick = {
                navigationHandler.navigate(NavigationAction.NavigateToArtistList)
            },
            headlineContent = { Text(text = "热门歌手") })

        ListItem(
            selected = false,
            onClick = {
                navigationHandler.navigate(NavigationAction.NavigateToAlbumSquare)
            },
            headlineContent = { Text(text = "专辑广场") })

        ListItem(
            selected = false,
            onClick = {
                navigationHandler.navigate(NavigationAction.NavigateToPlay)
            },
            headlineContent = { Text(text = "正在播放") })
    }
}