package com.ke.music.watch

import android.content.Intent
import com.arialyy.aria.core.Aria
import com.ke.music.common.BaseApplication
import com.ke.music.download.MusicDownloadService
import dagger.hilt.android.HiltAndroidApp

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
                reTryNum = 1
            }


    }
}