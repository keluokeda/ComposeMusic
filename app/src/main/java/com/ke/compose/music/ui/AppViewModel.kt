package com.ke.compose.music.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaMetadata
import android.media.browse.MediaBrowser
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.PlaybackState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.compose.music.domain.AddOrRemoveSongsToPlaylistRequest
import com.ke.compose.music.domain.AddOrRemoveSongsToPlaylistUseCase
import com.ke.compose.music.service.MusicDownloadService
import com.ke.compose.music.service.MusicPlayerService
import com.ke.compose.music.toast
import com.ke.compose.music.userIdFlow
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 全局ViewModel
 */
@HiltViewModel
class AppViewModel @Inject constructor(
    private val addOrRemoveSongsToPlaylistUseCase: AddOrRemoveSongsToPlaylistUseCase,
    @ApplicationContext private val context: Context
) : ViewModel(), IAppViewModel {

    private var _currentUserId = 0L

    override val currentUserId: Long
        get() = _currentUserId


    override var selectedSongList: List<Long> = emptyList()

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


        viewModelScope.launch {
            context.userIdFlow.collect {
                _currentUserId = it
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaBrowser.disconnect()
    }


    /**
     * 添加歌曲到歌单
     */
    override fun collectMusicsToPlaylist(musicIdList: List<Long>, playlistId: Long) {
        viewModelScope.launch {
            val request = AddOrRemoveSongsToPlaylistRequest(playlistId, musicIdList, true)
            addOrRemoveMusics(request)
        }
    }

    private suspend fun addOrRemoveMusics(request: AddOrRemoveSongsToPlaylistRequest) {
        addOrRemoveSongsToPlaylistUseCase(request)
        context.toast("操作成功")
    }

    override fun removeMusicsFromPlaylist(musicIdList: List<Long>, playlistId: Long) {
        viewModelScope.launch {
            val request = AddOrRemoveSongsToPlaylistRequest(playlistId, musicIdList, false)
            addOrRemoveMusics(request)
        }
    }

    override fun downloadMusic(musicId: Long) {
        val intent = Intent(context, MusicDownloadService::class.java)
        intent.action = MusicDownloadService.ACTION_DOWNLOAD_MUSIC
        intent.putExtra(MusicDownloadService.EXTRA_ID, musicId)
        context.startService(intent)
    }

    override fun downloadPlaylist(playlistId: Long) {
        val intent = Intent(context, MusicDownloadService::class.java)
        intent.action = MusicDownloadService.ACTION_DOWNLOAD_PLAYLIST
        intent.putExtra(MusicDownloadService.EXTRA_ID, playlistId)
        context.startService(intent)
    }


    override fun downloadAlbum(albumId: Long) {
        val intent = Intent(context, MusicDownloadService::class.java)
        intent.action = MusicDownloadService.ACTION_DOWNLOAD_ALBUM
        intent.putExtra(MusicDownloadService.EXTRA_ID, albumId)
        context.startService(intent)
    }

    override fun playMusic(musicId: Long) {
        controller?.transportControls?.playFromMediaId(musicId.toString(), null)
    }

}

interface IAppViewModel {

    fun collectMusicsToPlaylist(musicIdList: List<Long>, playlistId: Long)

    fun removeMusicsFromPlaylist(musicIdList: List<Long>, playlistId: Long)


    /**
     * 选中的歌曲列表
     */
    var selectedSongList: List<Long>


    /**
     * 下载音乐
     */
    fun downloadMusic(musicId: Long)


    /**
     * 下载歌单的全部歌曲
     */
    fun downloadPlaylist(playlistId: Long)

    fun downloadAlbum(albumId: Long)


    /**
     * 立刻播放一首音乐
     */
    fun playMusic(musicId: Long)


    val currentUserId: Long
}

private val defaultAppViewModel = object : IAppViewModel {
    override fun collectMusicsToPlaylist(musicIdList: List<Long>, playlistId: Long) {

    }

    override fun removeMusicsFromPlaylist(musicIdList: List<Long>, playlistId: Long) {

    }

    override var selectedSongList: List<Long> = emptyList()


    override fun downloadMusic(musicId: Long) {

    }


    override fun downloadPlaylist(playlistId: Long) {

    }

    override fun downloadAlbum(albumId: Long) {

    }

    override fun playMusic(musicId: Long) {

    }

    override val currentUserId: Long
        get() = 0L
}

val LocalAppViewModel = staticCompositionLocalOf<IAppViewModel> {
    defaultAppViewModel
}