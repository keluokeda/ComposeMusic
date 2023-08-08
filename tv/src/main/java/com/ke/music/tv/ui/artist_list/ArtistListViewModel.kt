package com.ke.music.tv.ui.artist_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ke.music.api.HttpService
import com.ke.music.repository.mediator.HotArtistListRemoteMediator
import com.ke.music.room.db.dao.HotArtistDao
import com.ke.music.room.db.entity.HotArtist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ArtistListViewModel @Inject constructor(
    httpService: HttpService,
    private val hotArtistDao: HotArtistDao
) : ViewModel() {


    private val _area = MutableStateFlow(ArtistArea.All)

    internal val area: StateFlow<ArtistArea>
        get() = _area

    internal fun updateArea(artistArea: ArtistArea) {
        if (remoteMediator.area == artistArea.value) {
            return
        }
        _area.value = artistArea
        remoteMediator.area = artistArea.value
    }

    private val _type = MutableStateFlow(ArtistType.All)

    internal val type: StateFlow<ArtistType>
        get() = _type

    internal fun updateType(artistType: ArtistType) {
        if (remoteMediator.type == artistType.value) {
            return
        }

        _type.value = artistType
        remoteMediator.type = artistType.value
    }


    private val remoteMediator =
        HotArtistListRemoteMediator(httpService, hotArtistDao, area.value.value, type.value.value)


    @OptIn(ExperimentalPagingApi::class)
    val artistList: Flow<PagingData<HotArtist>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false,
            initialLoadSize = 30
        ),
        remoteMediator = remoteMediator
    ) {
        hotArtistDao.getAll()
    }.flow
        .cachedIn(viewModelScope)

}