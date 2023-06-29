package com.ke.compose.music

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.arialyy.aria.core.Aria
import com.ke.compose.music.service.MusicDownloadService
import com.ke.music.api.response.Playlist
import com.ke.music.api.response.User
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date


@HiltAndroidApp
class MainApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        initLogger()


        startService(Intent(this, MusicDownloadService::class.java))

        Aria.get(this)
            .downloadConfig.apply {
//                maxSpeed = 1024
                isUseBlock = false
                threadNum = 1
                maxTaskNum = 1
            }
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

fun Playlist.convert(
    shareCount: Int = 0,
    bookedCount: Int = 0,
    commentCount: Int = 0
): com.ke.compose.music.db.entity.Playlist {
    return com.ke.compose.music.db.entity.Playlist(
        id,
        creator.userId,
        coverImgUrl,
        name,
        tags,
        description,
        trackCount,
        playCount,
        updateTime,
        shareCount, bookedCount, commentCount
    )
}

fun User.convert(): com.ke.compose.music.db.entity.User {
    return com.ke.compose.music.db.entity.User(userId, nickname, avatarUrl, signature)
}

// At the top level of your kotlin file:
private val Context.userIdStore: DataStore<Preferences> by preferencesDataStore(name = "user_id")

private val KEY_USER_ID = longPreferencesKey("userId")


/**
 * 用户id的流
 */
val Context.userIdFlow: Flow<Long>
    get() = userIdStore.data.map {

        it[KEY_USER_ID] ?: 0L
    }

suspend fun Context.getUserId() = userIdFlow.firstOrNull() ?: 0L


/**
 * 设置用户id
 */
suspend fun Context.setUserId(userId: Long) {
    userIdStore.edit {
        it[KEY_USER_ID] = userId
    }
}


//@Composable
//fun keyboardAsState(): State<Boolean> {
//    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
//    return rememberUpdatedState(isImeVisible)
//}


enum class Keyboard {
    Opened, Closed
}

@Composable
fun keyboardAsState(): State<Keyboard> {
    val keyboardState = remember { mutableStateOf(Keyboard.Closed) }
    val view = LocalView.current
    DisposableEffect(view) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val screenHeight = view.rootView.height
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                Keyboard.Opened
            } else {
                Keyboard.Closed
            }
        }
        view.viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}