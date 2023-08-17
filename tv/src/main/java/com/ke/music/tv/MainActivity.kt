package com.ke.music.tv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ke.music.tv.ui.navigation.AppNavigation
import com.ke.music.tv.ui.theme.ComposeMusicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeMusicTheme {
                AppNavigation()
            }
        }
    }
}

