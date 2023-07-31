package com.ke.compose.music.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.LocalNavigationHandler
import com.ke.compose.music.ui.component.NavigationAction
import com.ke.compose.music.ui.main.MainScreenFragment
import com.ke.compose.music.ui.theme.ComposeMusicTheme


@Composable
fun HomeRoute() {
    val homeViewModel: HomeViewModel = hiltViewModel()
    val refreshing by homeViewModel.refreshing.collectAsStateWithLifecycle()

    HomeScreen(refreshing = refreshing) {
        homeViewModel.refresh()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun HomeScreen(
    refreshing: Boolean, onRefresh: () -> Unit

) {


    val state = rememberPullRefreshState(refreshing, onRefresh)

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
    }) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(state)
        ) {

            val scrollState = rememberScrollState()

            Column(modifier = Modifier.verticalScroll(scrollState)) {
                val navigationHandler = LocalNavigationHandler.current
                ListItem(modifier = Modifier.clickable {
                    navigationHandler.navigate(NavigationAction.NavigateToPlaylistTop())
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

                ListItem(modifier = Modifier.clickable {
                    navigationHandler.navigate(NavigationAction.NavigateToRecommendSongs)
                }) {
                    Text(text = "每日推荐")
                }
            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = state,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    ComposeMusicTheme {
        HomeScreen(false) {

        }
    }
}