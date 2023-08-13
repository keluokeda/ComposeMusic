package com.ke.music.tv

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.domain.AddOrRemoveSongsToPlaylistRequest
import com.ke.music.repository.domain.AddOrRemoveSongsToPlaylistUseCase
import com.ke.music.repository.userIdFlow
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



    init {



        viewModelScope.launch {
            context.userIdFlow.collect {
                _currentUserId = it
            }
        }
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


}

interface IAppViewModel {

    fun collectMusicsToPlaylist(musicIdList: List<Long>, playlistId: Long)

    fun removeMusicsFromPlaylist(musicIdList: List<Long>, playlistId: Long)


    /**
     * 选中的歌曲列表
     */
    var selectedSongList: List<Long>


    val currentUserId: Long
}

private val defaultAppViewModel = object : IAppViewModel {
    override fun collectMusicsToPlaylist(musicIdList: List<Long>, playlistId: Long) {

    }

    override fun removeMusicsFromPlaylist(musicIdList: List<Long>, playlistId: Long) {

    }

    override var selectedSongList: List<Long> = emptyList()








    override val currentUserId: Long
        get() = 0L
}

val LocalAppViewModel = staticCompositionLocalOf<IAppViewModel> {
    defaultAppViewModel
}