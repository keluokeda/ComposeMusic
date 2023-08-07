package com.ke.music.pad.ui.main

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ke.music.pad.ui.playlist.PlaylistRoute
import com.ke.music.pad.ui.playlist_list.PlaylistListRoute
import com.ke.music.pad.ui.theme.ComposeMusicTheme

@Composable
fun MainRoute() {


    MainScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen() {

    val navHostController = rememberNavController()




    Scaffold { paddingValues ->

        PermanentNavigationDrawer(
            modifier = Modifier.padding(paddingValues),
            drawerContent = {
                Column(
                    modifier = Modifier
                        .width(200.dp)
                        .padding(16.dp)
                ) {

                    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    MainScreenFragment.values()
                        .forEach {
                            NavigationDrawerItem(
                                label = {
                                    Text(text = it.label)
                                },
                                icon = {
                                    Icon(imageVector = it.icon, contentDescription = null)
                                },
                                selected = it.route == currentDestination?.route,
                                onClick = {
                                    navHostController.navigate(it.route)
                                })
                        }

                }


            },
        ) {

            NavHost(
                navController = navHostController,
                startDestination = MainScreenFragment.Square.route
            ) {
                composable(MainScreenFragment.Square.route) {
//                    Text(text = "歌单广场")
                    PlaylistListRoute()
                }

                composable(MainScreenFragment.MyPlaylist.route) {
//                    Text(text = "我的歌单")
                    PlaylistRoute()
                }
            }

        }
    }


}

@Preview(device = Devices.TABLET, showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MainScreenPreview() {
    ComposeMusicTheme {
        MainScreen()
    }
}

private enum class MainScreenFragment(
    val route: String,
    val label: String,
    val icon: ImageVector
) {

    Square("/main/playlist/square", "歌单广场", Icons.Default.Home),

    MyPlaylist("/main/mine/playlist", "我的歌单", Icons.Default.Message)


}