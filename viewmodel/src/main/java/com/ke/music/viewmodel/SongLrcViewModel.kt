package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import com.ke.music.repository.domain.GetSongLrcUseCase
import com.ke.music.repository.domain.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongLrcViewModel @Inject constructor(private val getSongLrcUseCase: GetSongLrcUseCase) :
    ViewModel() {

    /**
     * 获取歌词文件
     */
    suspend fun getSongLrc(songId: Long) = getSongLrcUseCase(songId to false).successOr("")
}