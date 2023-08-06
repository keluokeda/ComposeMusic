package com.ke.music.player.service

import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.browse.MediaBrowser
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.PlaybackState
import androidx.compose.runtime.staticCompositionLocalOf
import com.orhanobut.logger.Logger

interface MusicPlayerController {
    /**
     * 立刻播放一首音乐
     */
    fun playMusic(musicId: Long)


    companion object {
        fun createMusicPlayerController(context: Context): MusicPlayerController {
            return MusicPlayerControllerImpl(context)
        }
    }
}

val LocalMusicPlayerController = staticCompositionLocalOf<MusicPlayerController> {
    object : MusicPlayerController {
        override fun playMusic(musicId: Long) {

        }

    }
}

internal class MusicPlayerControllerImpl(private val context: Context) : MusicPlayerController {

    private lateinit var mediaBrowser: MediaBrowser
    private var controller: MediaController? = null


    private val subscriptionCallback = object : MediaBrowser.SubscriptionCallback() {

    }

    private val mediaControllerCallback = object : MediaController.Callback() {
        override fun onMetadataChanged(metadata: MediaMetadata?) {
            Logger.d(metadata)
        }

        override fun onPlaybackStateChanged(state: PlaybackState?) {
//            super.onPlaybackStateChanged(state)
            Logger.d(state)

        }

        override fun onQueueChanged(queue: MutableList<MediaSession.QueueItem>?) {
            super.onQueueChanged(queue)
            Logger.d(queue)

        }
    }

    init {
        mediaBrowser = MediaBrowser(
            context,
            ComponentName(context, MusicPlayerService::class.java),
            object : MediaBrowser.ConnectionCallback() {
                override fun onConnected() {

                    Logger.d("onConnected")

                    if (mediaBrowser.isConnected) {
                        val mediaId = mediaBrowser.root
                        mediaBrowser.unsubscribe(mediaId)
                        mediaBrowser.subscribe(mediaId, subscriptionCallback)
                        controller =
                            MediaController(context, mediaBrowser.sessionToken)
                        controller?.registerCallback(mediaControllerCallback)

                    }
                }

                override fun onConnectionFailed() {
                    super.onConnectionFailed()
                    Logger.d("onConnectionFailed")
                }

                override fun onConnectionSuspended() {
                    super.onConnectionSuspended()
                    Logger.d("onConnectionSuspended")

                }
            },
            null
        )

        mediaBrowser.connect()


    }

    override fun playMusic(musicId: Long) {
        controller?.transportControls?.playFromMediaId(musicId.toString(), null)
    }

}