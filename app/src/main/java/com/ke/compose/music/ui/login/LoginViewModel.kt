package com.ke.compose.music.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.repository.domain.CreateQRUrlUseCase
import com.ke.music.repository.domain.LoginUseCase
import com.ke.music.repository.domain.Result
import com.ke.music.repository.domain.successOr
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val createQRUrlUseCase: CreateQRUrlUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _qrUrl = MutableStateFlow<String?>("")

    /**
     * null表示错误
     * 空表示加载中
     * 其他表示成功
     */
    internal val qrUrl: StateFlow<String?>
        get() = _qrUrl


    private val _loading = MutableStateFlow(false)
    internal val loading: StateFlow<Boolean>
        get() = _loading


    private val _navigationActions = Channel<Boolean>(capacity = Channel.CONFLATED)

    val navigationActions: Flow<Boolean>
        get() = _navigationActions.receiveAsFlow()

    private var key = ""

    init {
        refresh()
    }


    internal fun refresh() {
        viewModelScope.launch {
            _qrUrl.value = ""
            when (val result = createQRUrlUseCase(Unit)) {
                is Result.Error -> {
                    _qrUrl.value = null
                }

                is Result.Success -> {
                    _qrUrl.value = result.data.second
                    key = result.data.first
                }
            }
        }
    }

    internal fun startLogin() {
        viewModelScope.launch {
            _loading.value = true
            _navigationActions.send(loginUseCase(key).successOr(false))
            _loading.value = false
//            delay(5000)
//            _navigationActions.send(false)
        }
    }
}