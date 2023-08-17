package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.ke.music.common.entity.IMv
import com.ke.music.common.repository.MvRepository
import com.orhanobut.logger.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AllMvViewModel @Inject constructor(
    private val mvRepository: MvRepository,
    private val remoteMediator: com.ke.music.common.mediator.AllMvRemoteMediator,
) : ViewModel() {

    private val _area = MutableStateFlow("全部")

    val area: StateFlow<String>
        get() = _area

    private val _type = MutableStateFlow("全部")

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

//    private val remoteMediator = AllMvRemoteMediator(
//        httpService, mvRepository, area.value, type.value
//    )

    @OptIn(ExperimentalPagingApi::class)
    val allMv: Flow<PagingData<IMv>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false,
            initialLoadSize = 30
        ),
        remoteMediator = remoteMediator
    ) {
        Logger.d("数据发生了变化 重新加载数据 ${type.value} ${area.value}")
        mvRepository.getAllMv(area.value, type.value) as PagingSource<Int, IMv>
    }.flow
//        .map { data ->
//            data.map {
//                it
//            }
//        }

        .cachedIn(viewModelScope)
}