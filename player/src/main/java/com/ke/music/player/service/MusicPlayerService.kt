package com.ke.music.player.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.os.bundleOf
import androidx.media.MediaBrowserServiceCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerNotificationManager
import com.bumptech.glide.Glide
import com.ke.music.player.R
import com.ke.music.repository.MusicRepository
import com.ke.music.repository.domain.GetMusicUrlUseCase
import com.ke.music.room.entity.QueryDownloadedMusicResult
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlayerService : MediaBrowserServiceCompat(), Player.Listener,
    PlayerNotificationManager.NotificationListener {


    private fun updatePlaylist() {
        val list = exoPlayer.mediaItems.map {
            QueryDownloadedMusicResult(
                musicId = it.mediaId.toLong(),
                name = it.mediaMetadata.title.toString(),
                albumName = it.mediaMetadata.subtitle.toString(),
                albumImage = it.mediaMetadata.artworkUri?.toString() ?: "",
                path = null
            )
        }

        mediaSession.setExtras(
            bundleOf("playlist" to list)
        )
    }


    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        //只要是当前播放的音乐变了 都会走这个回调
        super.onMediaItemTransition(mediaItem, reason)
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)
//        mediaSession.setMetadata(
//            MediaMetadataCompat.fromMediaMetadata(mediaMetadata)
//        )

        //当前正在播放的音乐

        mediaSession.setMetadata(
            MediaMetadataCompat.Builder()
                .putText(MediaMetadataCompat.METADATA_KEY_TITLE, mediaMetadata.title)
                .putText(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, mediaMetadata.subtitle)
                .putString(
                    MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                    mediaMetadata.description?.toString() ?: ""
                )
                .putString(
                    MediaMetadataCompat.METADATA_KEY_ART_URI,
                    mediaMetadata.artworkUri?.toString() ?: ""
                )
                .build()
        )
    }


    override fun onTracksChanged(tracks: Tracks) {
        super.onTracksChanged(tracks)

        updatePlaylist()

        Logger.d("onTracksChanged $tracks")
    }


    override fun onIsPlayingChanged(isPlaying: Boolean) {
        //播放暂停变化
        super.onIsPlayingChanged(isPlaying)
        mediaSession.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(
                    if (isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                    0,
                    1f
                )
                .setExtras(
                    bundleOf(
                        "duration" to exoPlayer.duration
                    )
                )
                .build()
        )
    }


    override fun onPlaylistMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onPlaylistMetadataChanged(mediaMetadata)
    }

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var myNotificationManager: MyNotificationManager

    @Inject
    lateinit var getMusicUrlUseCase: GetMusicUrlUseCase

    @Inject
    lateinit var musicRepository: MusicRepository

    override fun onCreate() {
        super.onCreate()
        exoPlayer = ExoPlayer.Builder(this).build()

//        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        exoPlayer.addListener(this)

        // Build a PendingIntent that can be used to launch the UI.
        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, PendingIntent.FLAG_IMMUTABLE)
            }

        // Create a new MediaSession.
        mediaSession = MediaSessionCompat(this, "MusicService")
            .apply {
                setSessionActivity(sessionActivityPendingIntent)
                isActive = true
            }
        mediaSession.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPause() {
                super.onPause()
                exoPlayer.pause()
            }

            override fun onPlay() {
                super.onPlay()
                exoPlayer.play()
            }

            override fun onSkipToNext() {
                super.onSkipToNext()
//                exoPlayer.seekToNext()
                exoPlayer.seekToNextMediaItem()
            }

            override fun onSkipToPrevious() {
                super.onSkipToPrevious()
//                exoPlayer.seekToPrevious()
                exoPlayer.seekToPreviousMediaItem()
            }


            override fun onPlayFromMediaId(mediaId: String, extras: Bundle) {
                Logger.d("播放id位 $mediaId 的歌曲")

                serviceScope.launch {
                    val musicId = mediaId.toLongOrNull() ?: return@launch
                    val downloadedMusic = musicRepository.findDownloadedMusic(musicId)

                    if (downloadedMusic != null) {
                        playDownloadedMusic(downloadedMusic)
                    }
                }
            }
        })

        sessionToken = mediaSession.sessionToken

        myNotificationManager =
            MyNotificationManager(this, serviceScope, mediaSession.sessionToken, this)

        myNotificationManager.showNotificationForPlayer(exoPlayer)

        serviceScope.launch {
            while (true) {
                if (exoPlayer.isPlaying) {
                    val duration = exoPlayer.duration
                    val currentPosition = exoPlayer.currentPosition
//                    Logger.d("duration = $duration , currentPosition = $currentPosition")
                    mediaSession.setPlaybackState(
                        PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PLAYING, currentPosition, 1f)
                            .setExtras(
                                bundleOf("duration" to duration)
                            )
                            .build()

                    )
                }
                delay(1000)

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession.run {
            isActive = false
            release()
        }
        serviceJob.cancel()
        exoPlayer.release()

        Logger.d("MusicPlayerService 挂掉了")
    }


    /**
     * 播放下载的音乐
     */
    private fun playDownloadedMusic(music: QueryDownloadedMusicResult) {
        val item = MediaItem.Builder()
            .setUri(Uri.parse(music.path))
            .setMediaId(music.musicId.toString())
            .setMediaMetadata(
                MediaMetadata
                    .Builder()
                    .setTitle(music.name)
                    .setDescription(music.musicId.toString())
                    .setSubtitle(music.albumName)
                    .setArtworkUri(Uri.parse(music.albumImage))
                    .build()
            )
            .build()
        exoPlayer.addMediaItem(
            item
        )


        Logger.d("media list = ${exoPlayer.mediaItems.map { "${it.mediaMetadata.title} ${it.mediaMetadata.subtitle}" }}")


        exoPlayer.seekToDefaultPosition(exoPlayer.mediaItemCount - 1)


        exoPlayer.prepare()
    }


    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot {
        return BrowserRoot("root-id", null)
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.sendResult(null)
    }

    companion object {

        const val ACTION_PLAY_NOW = "ACTION_PLAY_NOW"
    }
}

/**
 * 获取所有的媒体项目
 */
private val ExoPlayer.mediaItems: List<MediaItem>
    get() {
        val mediaList = mutableListOf<MediaItem>()

        repeat(mediaItemCount) { index ->
            mediaList.add(getMediaItemAt(index))
        }
        return mediaList
    }

const val NOW_PLAYING_CHANNEL_ID = "com.example.android.uamp.media.NOW_PLAYING"
const val NOW_PLAYING_NOTIFICATION_ID = 0xb339 // Arbitrary number used to identify our notification

@SuppressLint("UnsafeOptInUsageError")
internal class MyNotificationManager(
    private val context: Context,
    private val coroutineScope: CoroutineScope,
    sessionToken: MediaSessionCompat.Token,
    notificationListener: PlayerNotificationManager.NotificationListener
) {

    private val notificationManager: PlayerNotificationManager

    init {
        val mediaController = MediaControllerCompat(context, sessionToken)

        val builder = PlayerNotificationManager.Builder(
            context,
            NOW_PLAYING_NOTIFICATION_ID,
            NOW_PLAYING_CHANNEL_ID
        )
        with(builder) {
            setMediaDescriptionAdapter(DescriptionAdapter(coroutineScope, mediaController, context))
            setNotificationListener(notificationListener)
            setChannelNameResourceId(R.string.notification_channel)

            setChannelDescriptionResourceId(R.string.notification_channel_description)
        }
        notificationManager = builder.build()
        notificationManager.setMediaSessionToken(sessionToken)
        notificationManager.setSmallIcon(R.drawable.baseline_music_note_24)
        notificationManager.setUseRewindAction(false)
        notificationManager.setUseFastForwardAction(false)
//        notificationManager.setUseFastForwardAction(true)
//        notificationManager.setUsePreviousAction(true)
//        notificationManager.setUseNextAction(true)

    }

    fun hideNotification() {
        notificationManager.setPlayer(null)
    }

    fun showNotificationForPlayer(player: Player) {
        notificationManager.setPlayer(player)
    }
}


@SuppressLint("UnsafeOptInUsageError")
private class DescriptionAdapter(
    private val coroutineScope: CoroutineScope,
    private val mediaController: MediaControllerCompat,
    private val context: Context
) : PlayerNotificationManager.MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): CharSequence {
//        return mediaController.metadata?.description?.title ?: ""
        return player.mediaMetadata.title ?: ""
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return mediaController.sessionActivity
    }

    override fun getCurrentContentText(player: Player): CharSequence? {
        return player.mediaMetadata.subtitle
    }


    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {

        val iconUri = player.mediaMetadata.artworkUri
        coroutineScope.launch {
            if (iconUri != null) {
                resolveUriAsBitmap(iconUri)?.apply {
                    callback.onBitmap(this)
                }
            }

        }

        return null

    }

    private suspend fun resolveUriAsBitmap(uri: Uri): Bitmap? {
        return withContext(Dispatchers.IO) {
            return@withContext Glide.with(context)
//                .applyDefaultRequestOptions(glideOptions)
                .asBitmap()
                .load(uri)
                .submit(NOTIFICATION_LARGE_ICON_SIZE, NOTIFICATION_LARGE_ICON_SIZE)
                .get()
        }
    }

}


const val NOTIFICATION_LARGE_ICON_SIZE = 144 // px