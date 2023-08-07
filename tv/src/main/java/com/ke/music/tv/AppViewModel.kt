package com.ke.music.tv

import android.content.ComponentName
import android.content.Context
import android.media.MediaMetadata
import android.media.browse.MediaBrowser
import android.media.session.MediaController
import android.media.session.MediaSession
import android.media.session.PlaybackState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.player.service.MusicPlayerService
import com.ke.music.repository.domain.AddOrRemoveSongsToPlaylistRequest
import com.ke.music.repository.domain.AddOrRemoveSongsToPlaylistUseCase
import com.ke.music.repository.userIdFlow
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
//        context.toast("操作成功")
    }

    override fun removeMusicsFromPlaylist(musicIdList: List<Long>, playlistId: Long) {
        viewModelScope.launch {
            val request = AddOrRemoveSongsToPlaylistRequest(playlistId, musicIdList, false)
            addOrRemoveMusics(request)
        }
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




    override fun playMusic(musicId: Long) {

    }



    override val currentUserId: Long
        get() = 0L
}

val LocalAppViewModel = staticCompositionLocalOf<IAppViewModel> {
    defaultAppViewModel
}