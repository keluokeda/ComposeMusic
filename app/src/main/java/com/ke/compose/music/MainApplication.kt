package com.ke.compose.music

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import com.arialyy.aria.core.Aria
import com.ke.music.common.BaseApplication
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


@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)

}