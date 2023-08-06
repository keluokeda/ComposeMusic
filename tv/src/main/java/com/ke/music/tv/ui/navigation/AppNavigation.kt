package com.ke.music.tv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ke.music.download.DownloadManagerImpl
import com.ke.music.download.LocalDownloadManager
import com.ke.music.tv.AppViewModel
import com.ke.music.tv.LocalAppViewModel
import com.ke.music.tv.ui.Screen
import com.ke.music.tv.ui.components.LocalNavigationHandler
import com.ke.music.tv.ui.downloaded.music.DownloadedMusicRoute
import com.ke.music.tv.ui.downloading.music.DownloadingMusicRoute
import com.ke.music.tv.ui.login.LoginScreen
import com.ke.music.tv.ui.playlist_detail.PlaylistDetailRoute
import com.ke.music.tv.ui.playlist_info.PlaylistInfoScreen
import com.ke.music.tv.ui.recommend_songs.RecommendSongsRoute
import com.ke.music.tv.ui.slpash.SplashScreen
import java.net.URLDecoder


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val appViewModel = hiltViewModel<AppViewModel>()

    CompositionLocalProvider(
        LocalNavigationHandler provides {
            navController.navigate(it.createPath())
        },
        LocalAppViewModel provides appViewModel,
        LocalDownloadManager provides DownloadManagerImpl(context)
    ) {
        NavigationTree(navController)
    }

}

@Composable
private fun NavigationTree(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen {
                if (it) {
//                    navController.navigate(Screen.PlaylistDetail.createUrl())
                    navController.navigate(Screen.RecommendSongs.route)
                } else {
                    navController.navigate(Screen.Login.route)
                }
            }
        }
        composable(Screen.Login.route) {
            LoginScreen {
                navController.navigate(Screen.PlaylistDetail.createUrl())
            }
        }
        composable(Screen.PlaylistDetail.route) {
            PlaylistDetailRoute(
                onCommentButtonClick = {},
                onPlaylistSubscribersClick = {},
                onCoverImageClick = {
                    navController.navigate(Screen.PlaylistInfo.createFromPlaylist(it))
                }
            )
        }

        composable(Screen.PlaylistInfo.route) {
            val argument = it.arguments!!
            PlaylistInfoScreen(
                name = argument.getString("name")!!,
                description = URLDecoder.decode(
                    argument.getString("description")!!,
                    Charsets.UTF_8.name()
                ),
                image = argument.getString("image")!!
            )
        }

        composable(Screen.DownloadingMusic.route) {
            DownloadingMusicRoute {

            }
        }

        composable(Screen.DownloadedMusic.route) {
            DownloadedMusicRoute {

            }
        }

        composable(Screen.RecommendSongs.route) {
            RecommendSongsRoute()
        }
    }
}
