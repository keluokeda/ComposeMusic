package com.ke.music.repository.mediator

//
//@OptIn(ExperimentalPagingApi::class)
//class UsersRemoteMediator(
//    private val userDao: UserDao,
//    private val httpService: HttpService,
//    private val sourceId: Long,
//    private val usersType: UsersType
//) : RemoteMediator<Int, User>() {
//
//    private var offset = 0
//
//    override suspend fun initialize(): InitializeAction {
//        return InitializeAction.LAUNCH_INITIAL_REFRESH
//    }
//
//
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, User>
//    ): MediatorResult {
//
//        Logger.d("开始加载数据 $loadType $offset")
//        when (loadType) {
//            LoadType.REFRESH -> {
////                userDao.deleteByTypeAndSourceId(usersType, sourceId)
//            }
//
//            LoadType.PREPEND -> {
//                return MediatorResult.Success(endOfPaginationReached = true)
//            }
//
//            LoadType.APPEND -> {
//                offset += state.config.pageSize
//            }
//        }
//
//
//        val provider: UsersProvider = when (usersType) {
//            UsersType.PlaylistSubscribers -> {
//                httpService.playlistSubscribers(sourceId, offset, state.config.pageSize)
//            }
//
//            UsersType.Follows -> {
//                throw IllegalArgumentException(usersType.name)
//            }
//        }
//
//
////        userDao.insertAll(provider.users().map { convert(it, usersType, sourceId) })
//
//
//        return MediatorResult.Success(
//            endOfPaginationReached = !provider.more()
//        )
//    }
//}

