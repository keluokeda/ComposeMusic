package com.ke.music.tv.ui.downloading.music

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.DownloadRepository
import com.ke.music.room.db.entity.Download
import com.ke.music.room.entity.QueryDownloadingMusicResult
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


    val all = downloadRepository.getDownloadingMusics()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    fun retry(id: Long) {
        viewModelScope.launch {
            downloadRepository.retryDownload(id)
        }
    }

    fun delete(queryDownloadingMusicResult: QueryDownloadingMusicResult) {
        if (queryDownloadingMusicResult.status == Download.STATUS_DOWNLOADING || queryDownloadingMusicResult.status == Download.STATUS_DOWNLOADED) {
            return
        }
        //只有失败的和还没开始下载的才能删除
        viewModelScope.launch {
            downloadRepository.delete(queryDownloadingMusicResult.id)
        }
    }
}