package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ke.music.api.HttpService
import com.ke.music.repository.MvRepository
import com.ke.music.repository.mediator.AllMvRemoteMediator
import com.ke.music.room.db.entity.Mv
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AllMvViewModel @Inject constructor(
    private val httpService: HttpService,
    private val mvRepository: MvRepository
) : ViewModel() {

    private val _area = MutableStateFlow<String>("全部")

    val area: StateFlow<String>
        get() = _area

    private val _type = MutableStateFlow<String>("全部")

    val type: StateFlow<String>
        get() = _type


    fun updateArea(value: String) {
        _area.value = value
        remoteMediator.area = value
    }

    fun updateType(value: String) {
        _type.value = value
        remoteMediator.type = value
    }

    private val remoteMediator = AllMvRemoteMediator(
        httpService, mvRepository, area.value, type.value
    )

    @OptIn(ExperimentalPagingApi::class)
    val allMv: Flow<PagingData<Mv>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false,
            initialLoadSize = 30
        ),
        remoteMediator = remoteMediator
    ) {
        mvRepository.getAllMv(area.value, type.value)
    }.flow
        .cachedIn(viewModelScope)
}