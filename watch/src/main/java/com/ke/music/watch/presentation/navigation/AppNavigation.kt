package com.ke.music.watch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.ke.music.watch.presentation.downloaded.DownloadedRoute
import com.ke.music.watch.presentation.downloading.DownloadingRoute
import com.ke.music.watch.presentation.local_playlist_songs.LocalPlaylistSongsRoute
import com.ke.music.watch.presentation.login.LoginRoute
import com.ke.music.watch.presentation.main.MainRoute
import com.ke.music.watch.presentation.my_playlist.MyPlaylistRoute
import com.ke.music.watch.presentation.play.PlayRoute
import com.ke.music.watch.presentation.playlist_detail.PlaylistDetailRoute
import com.ke.music.watch.presentation.splash.SplashRoute

@Composable
fun AppNavigation() {
    val controller = rememberSwipeDismissableNavController()
    SwipeDismissableNavHost(navController = controller, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashRoute { isLogin ->
                controller.navigate(if (isLogin) Screen.Play.route else Screen.Login.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }

//                controller.navigate(Screen.Play.route)
            }
        }

        composable(Screen.Login.route) {
            LoginRoute {

                if (it) {
                    controller.navigate(Screen.Play.route) {
                        popUpTo(Screen.Login.route) {
                            inclusive = true
                        }
                    }
                }


            }
        }

        composable(Screen.Main.route) {
            MainRoute({
                controller.navigate(Screen.MyPlaylist.route)
            }, {
                controller.navigate(Screen.Downloaded.route)
            }, {
                controller.navigate(Screen.Downloading.route)
            })
        }

        composable(Screen.MyPlaylist.route) {
            MyPlaylistRoute {
                controller.navigate(Screen.PlaylistDetail.createPath(it.id))
            }
        }

        composable(Screen.PlaylistDetail.route, arguments = listOf(
            navArgument("id") {
                type = NavType.LongType
            }
        )) {
            PlaylistDetailRoute()
        }

        composable(Screen.Downloaded.route) {
            DownloadedRoute()
        }

        composable(Screen.Play.route) {
            PlayRoute({
                controller.navigate(Screen.LocalPlaylistSongs.route)
            }) {
                controller.navigate(Screen.Main.route)
            }
        }

        composable(Screen.LocalPlaylistSongs.route) {
            LocalPlaylistSongsRoute()
        }

        composable(Screen.Downloading.route) {
            DownloadingRoute()
        }
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("/app/splash")

    object Login : Screen("/app/login")

    object Main : Screen("/app/main")

    object MyPlaylist : Screen("/mine/playlist")

    object PlaylistDetail : Screen("/playlist/{id}") {

        fun createPath(playlistId: Long) = "/playlist/$playlistId"
    }

    object Downloaded : Screen("/downloaded")

    object Downloading : Screen("/downloading")

    object Play : Screen("/play")

    object LocalPlaylistSongs : Screen("/localPlaylistSongs")
}