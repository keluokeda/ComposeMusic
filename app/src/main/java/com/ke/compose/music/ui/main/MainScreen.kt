package com.ke.compose.music.ui.main

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ke.compose.music.ui.home.HomeRoute
import com.ke.compose.music.ui.message.MessageRoute
import com.ke.compose.music.ui.mine.MineRoute
import com.ke.compose.music.ui.playlist.PlaylistRoute
import com.ke.music.room.db.entity.Playlist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainRoute(
    onTapPlaylist: (Playlist) -> Unit,
    onNewPlaylistClick: () -> Unit,

    ) {
    val navHostController = rememberNavController()

    Scaffold(bottomBar = {
        NavigationBar {
            val navBackStackEntry by navHostController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            listOf(
                MainScreenFragment.Home,
                MainScreenFragment.Message,
                MainScreenFragment.Playlist,
                MainScreenFragment.Mine
            ).forEach { item ->
                NavigationBarItem(selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                    onClick = {
                        navHostController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navHostController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }, label = {
                        Text(text = item.label)
                    }, icon = {
                        Icon(imageVector = item.icon, contentDescription = null)
                    })

            }
        }
    }) { padding ->

        NavHost(
            navController = navHostController,
            modifier = Modifier.statusBarsPadding(),
            startDestination = MainScreenFragment.Home.route
        ) {
            composable(MainScreenFragment.Home.route) {
                HomeRoute()
            }
            composable(MainScreenFragment.Message.route) {
                MessageRoute()
            }

            composable(MainScreenFragment.Playlist.route) {
                PlaylistRoute(onTapPlaylist, onNewPlaylistClick)
            }

            composable(MainScreenFragment.Mine.route) {
                MineRoute()
            }
        }
    }

}

sealed class MainScreenFragment(val route: String, val label: String, val icon: ImageVector) {

    object Home : MainScreenFragment("/main/home", "首页", Icons.Default.Home)

    object Message : MainScreenFragment("/main/message", "消息", Icons.Default.Message)

    object Playlist : MainScreenFragment("/main/playlist", "歌单", Icons.Default.LibraryMusic)

    object Mine : MainScreenFragment("/main/mine", "我的", Icons.Default.AccountCircle)
}