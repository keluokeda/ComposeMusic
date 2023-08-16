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
import com.ke.music.common.entity.ISongEntity
import com.ke.music.common.repository.SongRepository
import com.ke.music.player.R
import com.ke.music.repository.domain.GetMusicUrlUseCase
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class MusicPlayerService : MediaBrowserServiceCompat(), Player.Listener,
    PlayerNotificationManager.NotificationListener {


//    private fun updatePlaylist() {
//        val list = exoPlayer.mediaItems.map {
//            QueryDownloadedMusicResult(
//                musicId = it.mediaId.toLong(),
//                name = it.mediaMetadata.title.toString(),
//                albumName = it.mediaMetadata.subtitle.toString(),
//                albumImage = it.mediaMetadata.artworkUri?.toString() ?: "",
//                path = null
//            )
//        }

//        mediaSession.setExtras(
//            bundleOf("playlist" to list)
//        )

//        serviceScope.launch {
//            musicRepository.insertSongsToLocalPlaylist(
//                list.map { it.musicId }
//            )
//        }
//    }


    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        //只要是当前播放的音乐变了 都会走这个回调
        super.onMediaItemTransition(mediaItem, reason)
    }

    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
        super.onMediaMetadataChanged(mediaMetadata)
//        mediaSession.setMetadata(
//            MediaMetadataCompat.fromMediaMetadata(mediaMetadata)
//        )

        val songId = mediaMetadata.description?.toString()?.toLongOrNull()

        Logger.d("onMediaMetadataChanged $songId $mediaMetadata")


        if (songId == null) {
            return
        }


        serviceScope.launch {
            songRepository.onSongPlayed(songId)
        }

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
//        updatePlaylist()
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


    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var exoPlayer: ExoPlayer
    private lateinit var myNotificationManager: MyNotificationManager

    @Inject
    lateinit var getMusicUrlUseCase: GetMusicUrlUseCase

    @Inject
    lateinit var songRepository: SongRepository

    override fun onCreate() {
        super.onCreate()
        exoPlayer = ExoPlayer.Builder(this).build()

//        exoPlayer.prepare()
        exoPlayer.playWhenReady = true

        exoPlayer.addListener(this)
        exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ALL

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

            override fun onSeekTo(pos: Long) {
                super.onSeekTo(pos)
                exoPlayer.seekTo(pos)
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

                serviceScope.launch {
                    val musicId = mediaId.toLongOrNull() ?: return@launch
                    val result = songRepository.findDownloadedSong(musicId)
                    if (result != null) {
                        //已经下载了
                        if (exoPlayer.mediaItems.map { it.mediaId }.contains(musicId.toString())) {
                            //已经在列表中了
                            val index = exoPlayer.mediaItems.indexOfFirst {
                                it.mediaId == musicId.toString()
                            }
                            //播放选中的
                            exoPlayer.seekTo(index, 0)
                        } else {
                            //不在列表中 就插入进去

                            songRepository.insertSongIntoLocalPlaylist(
                                result.song.id
                            )

//                            exoPlayer.addMediaItem(
//                                MediaItem.Builder()
//                                    .setMediaId(mediaId)
//                                    .setMediaMetadata(
//                                        MediaMetadata.Builder()
//                                            .setTitle(result.name)
//                                            .setSubtitle(result.albumName)
//                                            .setDescription(mediaId)
//                                            .setArtworkUri(Uri.parse(result.albumImage))
//                                            .build()
//                                    )
//                                    .setUri(Uri.parse(result.path))
//                                    .build()
//                            )
                        }

                    }
                }
            }
        })

        sessionToken = mediaSession.sessionToken

        myNotificationManager =
            MyNotificationManager(this, serviceScope, mediaSession.sessionToken, this)

        myNotificationManager.showNotificationForPlayer(exoPlayer)


        //更新播放进度
        serviceScope.launch {
            while (true) {
                if (exoPlayer.isPlaying) {
                    val duration = exoPlayer.duration
                    val currentPosition = exoPlayer.currentPosition
//                    Logger.d("进度变化 $currentPosition , $duration")
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

        serviceScope.launch {
//            val list = musicRepository.getLocalPlaylistSongs()

//            musicRepository.getLocalPlaylistSongs()

            delay(1000)//经常是先调用后连上 会收不到第一次的回调
            songRepository
                .getLocalPlaylistSongs()
                .distinctUntilChanged()
                .collect { list ->
                    //本地播放列表发生了变化

                    exoPlayer.updateMediaItems(list)
//                    val currentMediaItem = exoPlayer.currentMediaItem
//                    val newList = list.map {
//                        entityToMediaItem(it)
//                    }
//
//                    if (currentMediaItem != null) {
//                        //当前有播放的
//                        val index = list.indexOfFirst {
//                            it.musicId.toString() == currentMediaItem.mediaId
//                        }
//                        if (index != -1) {
//                            //可能删除的就是当前正在播放的
//                            val currentPosition = exoPlayer.currentPosition
//                            //会卡顿一下
//                            exoPlayer.setMediaItems(
//                                newList, index, currentPosition
//                            )
//                        } else {
//                            exoPlayer.setMediaItems(
//                                newList
//                            )
//                        }
//
//                    } else {
//                        //没有播放
//
//                        exoPlayer.setMediaItems(
//                            newList
//                        )
//                    }
//
//
//
//                    exoPlayer.prepare()
                }
        }
    }

//    private fun entityToMediaItem(entity: DownloadedMusicEntity) = MediaItem.Builder()
//        .setUri(Uri.parse(entity.path))
//        .setMediaId(entity.musicId.toString())
//        .setMediaMetadata(
//            MediaMetadata
//                .Builder()
//                .setTitle(entity.name)
//                .setDescription(entity.musicId.toString())
//                .setSubtitle(entity.albumName)
//                .setArtworkUri(Uri.parse(entity.albumImage))
//                .build()
//        )
//        .build()

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

private fun ExoPlayer.updateMediaItems(list: List<ISongEntity>) {


    if (mediaItemCount == 0) {
        //播放列表为空
        setMediaItems(list.map {
            it.toMediaItem()
        })
        prepare()
        return
    }

    if (list.isEmpty()) {
        setMediaItems(emptyList())
        prepare()
        return
    }

    if (currentMediaItem == null) {
        //没有在播放
        setMediaItems(list.map {
            it.toMediaItem()
        })
        prepare()
        return
    }

    //旧的播放列表
    val mediaItemList = mediaItems


    //当前播放的歌曲在新列表中的位置
    val currentPlayingInNewListIndex = list.indexOfFirst {
        it.song.id.toString() == currentMediaItem?.mediaId
    }
    if (currentPlayingInNewListIndex == -1) {
        //表示把当前正在播放的给删除了
        setMediaItems(list.map {
            it.toMediaItem()
        })
        prepare()
        return
    }

    if (list.size - mediaItemList.size == 1) {
        //新增加了一个
        addMediaItem(list.last().toMediaItem())
        return
    }

    if (mediaItemList.size - list.size == 1) {
        //删除了一个 但删除的不是当前正在播放的 但删除的哪一个不知道
        val target =
            (mediaItemList.map { it.mediaId } - list.map { it.song.id.toString() }.toSet()).first()

        val index = mediaItemList.indexOfFirst {
            it.mediaId == target
        }

        removeMediaItem(index)
        return
    }


}

private fun ISongEntity.toMediaItem(): MediaItem {
    return MediaItem.Builder()
        .setMediaId(song.id.toString())
        .setMediaMetadata(
            MediaMetadata
                .Builder()
                .setTitle(song.name)
                .setSubtitle(album.name)
                .setDescription(song.id.toString())
                .setArtworkUri(
                    Uri.parse(album.image)
                )
                .build()
        )
        .setUri(Uri.parse(path))
        .build()
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