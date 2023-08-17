package com.ke.music.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.api.response.PrivateMessage
import com.ke.music.common.domain.GetUserMessagesUseCase
import com.ke.music.common.domain.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val getUserMessagesUseCase: GetUserMessagesUseCase,
) : ViewModel() {
    private val _list = MutableStateFlow<List<PrivateMessage>>(emptyList())

    val list: StateFlow<List<PrivateMessage>>
        get() = _list


    private val _refreshing = MutableStateFlow(false)
    val refreshing: StateFlow<Boolean>
        get() = _refreshing

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _refreshing.value = true
            _list.value = getUserMessagesUseCase(Unit).successOr(emptyList())
            _refreshing.value = false
        }
    }
}