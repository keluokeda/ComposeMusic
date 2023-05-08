package com.ke.compose.music

import android.annotation.SuppressLint
import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.HiltAndroidApp
import java.text.SimpleDateFormat
import java.util.Date


@HiltAndroidApp
class MainApplication : Application() {

    companion object {
        ///当前用户id
        var currentUserId: Long = 0
    }

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