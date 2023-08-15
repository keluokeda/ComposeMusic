package com.ke.music.common

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

abstract class BaseApplication : Application() {

//    override fun newImageLoader(): ImageLoader {
//        val loader =
//            ImageLoader(this).newBuilder()
//                .memoryCache {
//                    MemoryCache.Builder(applicationContext)
//                        .maxSizePercent(.4)
//                        .build()
//                }
//                .diskCache {
//
//                    DiskCache.Builder()
//                        .maxSizePercent(.2)
//                        .build()
//                }
//
//                .build()
//
//        return loader
//    }

    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(true)
            .methodCount(5)
            .tag("logger")
            .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return true
            }
        })
    }
}

@Composable
inline fun <reified T> Flow<T>.observeWithLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    noinline action: suspend (T) -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.lifecycleScope.launch {
            flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).collect(action)
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

