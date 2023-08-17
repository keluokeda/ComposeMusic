package com.ke.compose.music.ui.users

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ke.music.common.entity.IUser
import com.ke.music.repository.entity.UsersType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(

    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val usersType = savedStateHandle.get<UsersType>("type")!!
    private val sourceId = savedStateHandle.get<Long>("id")!!

    internal val title = savedStateHandle.get<String>("title")!!

//    @OptIn(ExperimentalPagingApi::class)
//    internal val users = Pager(
//        config = PagingConfig(
//            pageSize = 50,
//            enablePlaceholders = false,
//            initialLoadSize = 50
//        ),
//        remoteMediator = UsersRemoteMediator(userDao, httpService, sourceId, usersType)
//    ) {
//        userDao.getUsers(usersType, sourceId)
//    }
//        .flow.cachedIn(viewModelScope)


    internal fun toggleFollow(user: IUser) {
//        viewModelScope.launch {
//            userDao.updateUser(
//                user.copy(
//                    followed = !user.followed
//                )
//            )
//
//            followUserUseCase(user.userId to !user.followed)
//        }
    }
}