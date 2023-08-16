package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.common.repository.DownloadRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DownloadingMusicViewModel @Inject constructor(
    private val downloadRepository: DownloadRepository
) :
    ViewModel() {


    val all = downloadRepository.getDownloadingSongs()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun retry(id: Long) {
        viewModelScope.launch {
            downloadRepository.retryDownload(id)
        }
    }

    fun delete(id: Long) {

        //只有失败的和还没开始下载的才能删除
        viewModelScope.launch {
            downloadRepository.delete(id)
        }
    }
}