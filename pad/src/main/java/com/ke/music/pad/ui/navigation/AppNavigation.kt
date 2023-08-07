package com.ke.music.pad.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ke.music.download.DownloadManagerImpl
import com.ke.music.download.LocalDownloadManager
import com.ke.music.pad.ui.Screen
import com.ke.music.pad.ui.component.LocalNavigationHandler
import com.ke.music.pad.ui.login.LoginScreen
import com.ke.music.pad.ui.main.MainRoute
import com.ke.music.pad.ui.slpash.SplashScreen
import com.ke.music.player.service.LocalMusicPlayerController
import com.ke.music.player.service.MusicPlayerController


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
//    val appViewModel = hiltViewModel<AppViewModel>()
    val context = LocalContext.current
    CompositionLocalProvider(
        LocalNavigationHandler provides {
            navController.navigate(it.createPath())
        },
//        LocalBackHandler provides {
//            navController.popBackStack()
//        },
//        LocalAppViewModel provides appViewModel,
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
            MainRoute()
        }

    }
}
