package com.ke.compose.music.ui

import android.content.Context
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.compose.music.domain.AddOrRemoveSongsToPlaylistRequest
import com.ke.compose.music.domain.AddOrRemoveSongsToPlaylistUseCase
import com.ke.compose.music.toast
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


    override var selectedSongList: List<Long> = emptyList()


    /**
     * 添加歌曲到歌单
     */
    override fun collectSongsToPlaylist(songIds: List<Long>, playlistId: Long) {
        viewModelScope.launch {
            val request = AddOrRemoveSongsToPlaylistRequest(playlistId, songIds, true)
            addOrRemoveSongsToPlaylistUseCase(request)
            context.toast("保存成功")
        }
    }
}

interface IAppViewModel {

    fun collectSongsToPlaylist(songIds: List<Long>, playlistId: Long)

    /**
     * 选中的歌曲列表
     */
    var selectedSongList: List<Long>
}

private val defaultAppViewModel = object : IAppViewModel {
    override fun collectSongsToPlaylist(songIds: List<Long>, playlistId: Long) {

    }

    override var selectedSongList: List<Long> = emptyList()


}

val LocalAppViewModel = staticCompositionLocalOf {
    defaultAppViewModel as IAppViewModel
}