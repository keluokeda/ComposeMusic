package com.ke.compose.music

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import com.arialyy.aria.core.Aria
import com.ke.music.commom.BaseApplication
import com.ke.music.download.MusicDownloadService
import dagger.hilt.android.HiltAndroidApp
import java.text.SimpleDateFormat
import java.util.Date


@HiltAndroidApp
class MainApplication : BaseApplication() {


    override fun onCreate() {
        super.onCreate()


        startService(Intent(this, MusicDownloadService::class.java))

        Aria.get(this)
            .downloadConfig.apply {
                isUseBlock = false
                threadNum = 1
                maxTaskNum = 1
            }
    }


}

fun Long.niceTime(): String {
    val date = Date(this)
    return simpleDateFormat.format(date)
}

@SuppressLint("SimpleDateFormat")
private val simpleDateFormat = SimpleDateFormat("HH:mm")

fun Int.niceCount(): String {
    if (this < 10000) {
        return this.toString()
    }
    return "${this / 10000}万"
}


enum class Keyboard {
    Opened, Closed
}

@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
//    DisposableEffect(view) {
//        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
//            val rect = Rect()
//            view.getWindowVisibleDisplayFrame(rect)
//            val screenHeight = view.rootView.height
//            val keypadHeight = screenHeight - rect.bottom
//            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
//                Keyboard.Opened
//            } else {
//                Keyboard.Closed
//            }
//        }
//        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)
//
//        onDispose {
//            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
//        }
//    }

    return keyboardState
}