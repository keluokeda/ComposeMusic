package com.ke.music.player.service

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.compose.runtime.staticCompositionLocalOf
import com.ke.music.common.entity.DownloadStatus
import com.ke.music.common.entity.IAlbum
import com.ke.music.common.entity.IArtist
import com.ke.music.common.entity.ISong
import com.ke.music.common.entity.ISongEntity
import com.orhanobut.logger.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize

interface MusicPlayerController {
    /**
     * 立刻播放一首音乐
     */
    fun playMusic(musicId: Long)


    /**
     * 暂停
     */
    fun pause()

    /**
     * 播放
     */
    fun play()

    /**
     * 播放下一首
     */
    fun skipToNext()


    /**
     * 播放上一首
     */
    fun skipToPrevious()


    /**
     * 跳转
     */
    fun seekTo(position: Long)


    /**
     * 是否正在播放
     */
    val isPlaying: StateFlow<Boolean>

    /**
     * 播放进度
     */
    val progress: StateFlow<Pair<Long, Long>>

    val currentPlaying: StateFlow<ISongEntity?>

    companion object {
        fun createMusicPlayerController(context: Context): MusicPlayerController {
            return MusicPlayerControllerImpl(context)
        }
    }


}

private object EmptyMusicPlayerController : MusicPlayerController {
    override fun playMusic(musicId: Long) {
    }

    override fun pause() {
    }

    override fun play() {
    }

    override val isPlaying: StateFlow<Boolean>
        get() = MutableStateFlow(false)
    override val progress: StateFlow<Pair<Long, Long>>
        get() = MutableStateFlow(0L to 0L)

    override fun skipToNext() {
    }

    override fun skipToPrevious() {

    }

    override val currentPlaying: StateFlow<ISongEntity?>
        get() = MutableStateFlow(null)

    override fun seekTo(position: Long) {

    }

}

val LocalMusicPlayerController = staticCompositionLocalOf<MusicPlayerController> {
    EmptyMusicPlayerController
}


private class SongEntity(
    override val song: ISong,
    override val album: IAlbum,
    override val artists: List<IArtist>,
    override val status: DownloadStatus,
    override val path: String?,
) : ISongEntity

private class Song(
    override val id: Long,
    override val name: String,
    override val albumId: Long,
    override val mv: Long,
) : ISong

@Parcelize
private class Album(
    override val albumId: Long,
    override val name: String,
    override val image: String,
    override val key: Long,
) : IAlbum

internal class MusicPlayerControllerImpl(private val context: Context) : MusicPlayerController {

    private lateinit var mediaBrowser: MediaBrowserCompat
    private var controller: MediaControllerCompat? = null

    private val _isPlaying = MutableStateFlow(true)

    private val _progress = MutableStateFlow(0L to 0L)


    private val _currentPlaying = MutableStateFlow<ISongEntity?>(null)


    override val currentPlaying: StateFlow<ISongEntity?>
        get() = _currentPlaying
    override val progress: StateFlow<Pair<Long, Long>>
        get() = _progress

    override val isPlaying: StateFlow<Boolean>
        get() = _isPlaying


    private val subscriptionCallback = object : MediaBrowserCompat.SubscriptionCallback() {

    }

    private val mediaControllerCallback = object : MediaControllerCompat.Callback() {


//        override fun onExtrasChanged(extras: Bundle?) {
//            super.onExtrasChanged(extras)
//            val playlist = extras?.getParcelableArrayList<QueryDownloadedMusicResult>("playlist")
//
//            Logger.d("正在播放的音乐列表发生了变化 $playlist")
//        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat) {


            val title = metadata.getText(MediaMetadataCompat.METADATA_KEY_TITLE).toString()
            val albumName =
                metadata.getText(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE).toString()
            val image = metadata.getString(MediaMetadataCompat.METADATA_KEY_ART_URI)
            val id = metadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID).toLong()
//            super.onMetadataChanged(metadata)

            _currentPlaying.value = SongEntity(
                song = Song(id, title, 0, 0),
                album = Album(0, albumName, image, 0),
                artists = emptyList(),
                path = null,
                status = DownloadStatus.Downloaded
            )
//                QueryDownloadedMusicResult(
//                    id, title, 0, albumName, image, null
//                )
            _progress.value = 0L to 0

        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            super.onPlaybackStateChanged(state)

            if (state == null) {
                return
            }


            when (state.state) {
                PlaybackStateCompat.STATE_PLAYING -> {
                    _isPlaying.value = true
                    if (state.position != 0L) {
                        //暂停播放会出现0进度
                        _progress.value =
                            state.position to (state.extras?.getLong("duration") ?: 0L)
                    }

//                    Logger.d("进度发生变化 ${progress.value}")
                }

                PlaybackStateCompat.STATE_PAUSED -> {
                    _isPlaying.value = false
                }

                else -> {

                }
            }
        }


        override fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>) {
            super.onQueueChanged(queue)

        }

    }

    init {
        mediaBrowser = MediaBrowserCompat(
            context,
            ComponentName(context, MusicPlayerService::class.java),
            object : MediaBrowserCompat.ConnectionCallback() {
                override fun onConnected() {

                    Logger.d("onConnected")

                    if (mediaBrowser.isConnected) {
                        val mediaId = mediaBrowser.root
                        mediaBrowser.unsubscribe(mediaId)
                        mediaBrowser.subscribe(mediaId, subscriptionCallback)
                        controller =
                            MediaControllerCompat(context, mediaBrowser.sessionToken)
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

    override fun pause() {
        controller?.transportControls?.pause()
    }

    override fun play() {
        controller?.transportControls?.play()
    }

    override fun skipToNext() {
        controller?.transportControls?.skipToNext()
    }

    override fun skipToPrevious() {
        controller?.transportControls?.skipToPrevious()
    }

    override fun seekTo(position: Long) {
        controller?.transportControls?.seekTo(position)
    }
}