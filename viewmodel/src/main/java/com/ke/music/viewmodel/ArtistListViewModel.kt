package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.ke.music.common.entity.IArtist
import com.ke.music.common.mediator.HotArtistRemoteMediator
import com.ke.music.common.repository.ArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ArtistListViewModel @Inject constructor(
    private val artistRepository: ArtistRepository,
    private val hotArtistRemoteMediator: HotArtistRemoteMediator,
) : ViewModel() {


    private val _area = MutableStateFlow(ArtistArea.All)

    val area: StateFlow<ArtistArea>
        get() = _area

    fun updateArea(artistArea: ArtistArea) {
        _area.value = artistArea
        hotArtistRemoteMediator.area = artistArea.value
    }

    private val _type = MutableStateFlow(ArtistType.All)

    val type: StateFlow<ArtistType>
        get() = _type

    fun updateType(artistType: ArtistType) {
        _type.value = artistType
        hotArtistRemoteMediator.type = artistType.value
    }


//    private val remoteMediator =
//        HotArtistListRemoteMediator(httpService, hotArtistDao, area.value.value, type.value.value)


    @OptIn(ExperimentalPagingApi::class)
    val artistList: Flow<PagingData<IArtist>> = Pager(
        config = PagingConfig(
            pageSize = 30,
            enablePlaceholders = false,
            initialLoadSize = 30
        ),
        remoteMediator = hotArtistRemoteMediator
    ) {
        artistRepository.hotArtists(
            type.value.value,
            area.value.value
        ) as PagingSource<Int, IArtist>
    }.flow
        .cachedIn(viewModelScope)

}

enum class ArtistArea(val title: String, val value: Int) {
    All("全部", -1),
    China("华语", 7),
    EA("欧美", 96),
    Japan("日本", 8),
    Korea("韩国", 16),
    Other("其他", 0)
}

enum class ArtistType(val title: String, val value: Int) {
    All("全部", -1),
    Man("男歌手", 1),
    Women("女歌手", 2),
    Band("乐队", 3)
}