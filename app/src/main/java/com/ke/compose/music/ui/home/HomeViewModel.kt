package com.ke.compose.music.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _refreshing = MutableStateFlow(false)
    internal val refreshing: StateFlow<Boolean>
        get() = _refreshing

    internal fun refresh() {
        viewModelScope.launch {
            _refreshing.value = true
            delay(3000)
            _refreshing.value = false
        }
    }
}