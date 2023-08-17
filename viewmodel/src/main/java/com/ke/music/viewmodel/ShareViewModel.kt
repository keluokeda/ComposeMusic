package com.ke.music.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ke.music.api.response.User
import com.ke.music.common.domain.GetShareUsersUseCase
import com.ke.music.common.domain.ShareResourceUseCase
import com.ke.music.common.domain.successOr
import com.ke.music.common.entity.ShareRequest
import com.ke.music.common.entity.ShareType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.URLDecoder
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getShareUsersUseCase: GetShareUsersUseCase,
    private val shareResourceUseCase: ShareResourceUseCase
) : ViewModel() {


    val type = savedStateHandle.get<ShareType>("type")!!
    val id = savedStateHandle.get<Long>("id")!!
    val title = savedStateHandle.get<String>("title")!!
    val subTitle = URLDecoder.decode(
        savedStateHandle.get<String>("subTitle")!!,
        Charsets.UTF_8.name()
    )
    val cover = savedStateHandle.get<String>("cover")!!

    private val _users = MutableStateFlow<List<User>>(emptyList())

    val users: StateFlow<List<User>>
        get() = _users

    private val _sending = MutableStateFlow(false)
    val sending: StateFlow<Boolean>
        get() = _sending

    private val _sendResult = Channel<Boolean>(capacity = Channel.CONFLATED)

    val sendResult: Flow<Boolean>
        get() = _sendResult.receiveAsFlow()

    init {
        viewModelScope.launch {
            _users.value = getShareUsersUseCase(Unit).successOr(emptyList())
        }
    }

    fun share(users: List<User>, content: String) {
        viewModelScope.launch {
            _sending.value = true
            val result = shareResourceUseCase(
                ShareRequest(
                    type,
                    id,
                    content,
                    users.map { it.userId })
            ).successOr(false)
            _sending.value = false
            _sendResult.send(result)
        }
    }
}

