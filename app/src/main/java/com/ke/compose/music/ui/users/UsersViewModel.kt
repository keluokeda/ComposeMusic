package com.ke.compose.music.ui.users

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ke.compose.music.db.dao.UserDao
import com.ke.compose.music.db.entity.User
import com.ke.compose.music.domain.FollowUserUseCase
import com.ke.music.api.HttpService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val userDao: UserDao,
    private val followUserUseCase: FollowUserUseCase,
    httpService: HttpService,
    savedStateHandle: SavedStateHandle
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


    internal fun toggleFollow(user: User) {
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