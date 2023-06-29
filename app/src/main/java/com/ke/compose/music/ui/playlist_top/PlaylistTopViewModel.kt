package com.ke.compose.music.ui.playlist_top

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ke.music.api.HttpService
import com.ke.music.api.response.Playlist
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistTopViewModel @Inject constructor(
    private val httpService: HttpService
) :
    ViewModel() {

    private var topPlaylistPagingSource: TopPlaylistPagingSource? = null

    private val _category = MutableStateFlow<String?>(null)
    internal val category: StateFlow<String?>
        get() = _category

    val pagingData: Flow<PagingData<Playlist>> = Pager(
        config = PagingConfig(50, enablePlaceholders = false)
    ) {
        TopPlaylistPagingSource(httpService, category.value).apply {
            topPlaylistPagingSource = this
        }
    }.flow.cachedIn(viewModelScope)


    init {
        viewModelScope.launch {
            category.collect {
                topPlaylistPagingSource?.invalidate()
            }
        }
    }

    internal fun updateCategory(categoryString: String?) {
        _category.value = categoryString
    }
}