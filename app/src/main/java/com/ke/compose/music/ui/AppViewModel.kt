package com.ke.compose.music.ui

import android.annotation.SuppressLint
import android.content.Context
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
) : ViewModel() {


    /**
     * 添加歌曲到歌单
     */
    internal fun collectSongsToPlaylist(songIds: List<Long>, playlistId: Long) {
        viewModelScope.launch {
            val request = AddOrRemoveSongsToPlaylistRequest(playlistId, songIds, true)
            addOrRemoveSongsToPlaylistUseCase(request)
            context.toast("保存成功")
        }
    }
}