package com.ke.compose.music.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import com.ke.compose.music.ui.AppViewModel
import com.ke.compose.music.ui.LocalAppViewModel
import com.ke.compose.music.ui.Screen
import com.ke.compose.music.ui.album.detail.AlbumDetailRoute
import com.ke.compose.music.ui.album_square.AlbumSquareRoute
import com.ke.compose.music.ui.artist_list.ArtistListRoute
import com.ke.compose.music.ui.child_comments.ChildCommentsRoute
import com.ke.compose.music.ui.comments.CommentsRoute
import com.ke.compose.music.ui.component.LocalBackHandler
import com.ke.compose.music.ui.component.LocalNavigationHandler
import com.ke.compose.music.ui.downloaded.music.DownloadedMusicRoute
import com.ke.compose.music.ui.downloading.music.DownloadingMusicRoute
import com.ke.compose.music.ui.login.LoginScreen
import com.ke.compose.music.ui.main.MainRoute
import com.ke.compose.music.ui.playlist_category.PlaylistCategoryRoute
import com.ke.compose.music.ui.playlist_detail.PlaylistDetailRoute
import com.ke.compose.music.ui.playlist_highquality.PlaylistHighqualityRoute
import com.ke.compose.music.ui.playlist_info.PlaylistInfoScreen
import com.ke.compose.music.ui.playlist_list.PlaylistListRoute
import com.ke.compose.music.ui.playlist_new.PlaylistNewScreen
import com.ke.compose.music.ui.playlist_top.PlaylistTopRoute
import com.ke.compose.music.ui.recommend_songs.RecommendSongsRoute
import com.ke.compose.music.ui.share.ShareRoute
import com.ke.compose.music.ui.slpash.SplashScreen
import com.ke.compose.music.ui.users.UsersRoute
import com.ke.music.download.DownloadManagerImpl
import com.ke.music.download.LocalDownloadManager
import com.ke.music.player.service.LocalMusicPlayerController
import com.ke.music.player.service.MusicPlayerController
import com.ke.music.repository.entity.ShareType
import com.ke.music.repository.entity.UsersType
import com.ke.music.room.entity.CommentType
import java.net.URLDecoder


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val appViewModel = hiltViewModel<AppViewModel>()
    val context = LocalContext.current
    CompositionLocalProvider(
        LocalNavigationHandler provides {
            navController.navigate(it.createPath())
        },
        LocalBackHandler provides {
            navController.popBackStack()
        },
        LocalAppViewModel provides appViewModel,
        LocalDownloadManager provides DownloadManagerImpl(context),
        LocalMusicPlayerController provides MusicPlayerController.createMusicPlayerController(
            context
        )
    ) {
        NavigationTree(navController)
    }

}

@Composable
private fun NavigationTree(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Splash.route) {
        composable(Screen.Login.route) {
            LoginScreen {
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Login.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(Screen.Splash.route) {
            SplashScreen {
                navController.navigate(if (it) Screen.Main.route else Screen.Login.route) {
                    popUpTo(Screen.Splash.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(Screen.Main.route) {
            MainRoute({
                navController.navigate(Screen.PlaylistDetail.createUrl(it))
            }, {
                navController.navigate(Screen.PlaylistNew.route)
            })

        }

        composable(Screen.PlaylistDetail.route) {
            PlaylistDetailRoute({
                navController.popBackStack()
            }, {
                navController.navigate(
                    Screen.Comments.createMusicComment(
                        CommentType.Playlist,
                        it
                    )
                )
            }, {
                navController.navigate(
                    Screen.Users.createPath(
                        "订阅者",
                        it,
                        UsersType.PlaylistSubscribers
                    )
                )
            }) {
                navController.navigate(Screen.PlaylistInfo.createFromPlaylist(it))
            }
        }

        composable(Screen.Comments.route, arguments = listOf(navArgument("id") {
            type = NavType.LongType
        }, navArgument("type") {
            type = NavType.EnumType(CommentType::class.java)
        })) {
            CommentsRoute({
                navController.popBackStack()
            }, { id, type, commentId ->
                navController.navigate(Screen.ChildComment.createPath(type, id, commentId))
            })
        }

        composable(
            Screen.ChildComment.route, arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType
                },
                navArgument("type") {
                    type = NavType.EnumType(CommentType::class.java)
                }, navArgument("commentId") {
                    type = NavType.LongType
                }
            )

        ) {
            ChildCommentsRoute {
                navController.popBackStack()
            }
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
            ) {
                navController.popBackStack()
            }
        }

        composable(Screen.PlaylistNew.route) {
            PlaylistNewScreen(onBackButtonClick = {
                navController.popBackStack()
            }) {
                navController.popBackStack()
            }
        }

        composable(Screen.Share.route, arguments = listOf(
            navArgument("type") {
                type = NavType.EnumType(ShareType::class.java)
            }, navArgument("id") {
                type = NavType.LongType
            }, navArgument("title") {
                type = NavType.StringType
            }, navArgument("subTitle") {
                type = NavType.StringType
            }, navArgument("cover") {
                type = NavType.StringType
            }
        )) {
            ShareRoute {
                navController.popBackStack()
            }
        }

        composable(Screen.Users.route, arguments = listOf(
            navArgument("title") {
                type = NavType.StringType
            },
            navArgument("id") {
                type = NavType.LongType
            },
            navArgument("type") {
                type = NavType.EnumType(UsersType::class.java)
            }
        )) {
            UsersRoute()
        }

        composable(Screen.AlbumDetail.route, arguments = listOf(
            navArgument("id") {
                type = NavType.LongType
            }
        )) {
            AlbumDetailRoute()
        }

        composable(
            Screen.PlaylistList.route
        ) {

            val appViewModel = LocalAppViewModel.current
            PlaylistListRoute({
                appViewModel.collectMusicsToPlaylist(
                    appViewModel.selectedSongList, it
                )
                navController.popBackStack()
            }) {
                navController.navigate(Screen.PlaylistNew.route)
            }
        }

        composable(
            Screen.PlaylistTop.route,
            arguments = listOf(
                navArgument("category") {
                    type = NavType.StringType
                    defaultValue = "全部"
                }
            )
        ) {
//            navController.GetOnceResult<String>(keyResult = "result", onResult = {
//                Logger.d("收到了上个页面的结果 $it")
//            })
//            val category by it.savedStateHandle.getLiveData<String>("result").observeAsState()

//            val category = it.savedStateHandle.get<String>("category")
            PlaylistTopRoute(
                onBackButtonClick = { navController.popBackStack() },
                onItemClick = { playlist ->
                    navController.navigate(Screen.PlaylistDetail.createUrl(playlist))
                },
                onCategoryButtonClick = {
                    navController.navigate(Screen.PlaylistCategory.route)
                }
            )
        }

        composable(Screen.PlaylistCategory.route) {
            PlaylistCategoryRoute(onBackButtonClick = {
                navController.popBackStack()
            }, onCategoryClick = {
//                navController.previousBackStackEntry
//                    ?.savedStateHandle
//                    ?.set("category", it)
//                navController.popBackStack()

//                navController.navigate(Screen.PlaylistTop.createPath(it), navOptions {
//
//                })

                navController.navigate(Screen.PlaylistTop.createPath(it), navOptions {
                    popUpTo(Screen.Main.route) {
                        inclusive = false
                    }

                })
            })
        }

        composable(Screen.HighqualityPlaylist.route) {
            PlaylistHighqualityRoute({
                navController.popBackStack()
            }) {

                navController.navigate(Screen.PlaylistDetail.createUrl(it))

            }
        }

        composable(Screen.DownloadedMusic.route) {
            DownloadedMusicRoute {
                navController.popBackStack()
            }
        }

        composable(Screen.DownloadingMusic.route) {
            DownloadingMusicRoute {
                navController.popBackStack()
            }
        }

        composable(Screen.RecommendSongs.route) {
            RecommendSongsRoute {
                navController.popBackStack()
            }
        }

        composable(Screen.AlbumSquare.route) {
            AlbumSquareRoute()
        }

        composable(Screen.ArtistList.route) {
            ArtistListRoute()
        }
    }
}
