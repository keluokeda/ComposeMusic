package com.ke.music.repository.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import com.ke.music.api.HttpService
import com.ke.music.common.entity.IMv
import com.ke.music.common.mediator.AllMvRemoteMediator
import com.ke.music.common.repository.MvRepository
import com.orhanobut.logger.BuildConfig
import javax.inject.Inject

internal class AllMvRemoteMediator @Inject constructor(
    private val httpService: HttpService,
    private val mvRepository: MvRepository,
) :
    AllMvRemoteMediator() {

    override var area: String = "全部"
    override var type: String = "全部"

    private var offset = 0

    @ExperimentalPagingApi
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, IMv>,
    ): MediatorResult {
        try {

            when (loadType) {
                LoadType.REFRESH -> {
                    offset = 0
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    offset += 30
                }
            }

            val response = httpService.getAllMv(type, area, 30, offset)

            val deleteOld = offset == 0

            mvRepository.saveAllMv(area, type, response.data, deleteOld)

            return MediatorResult.Success(
                endOfPaginationReached = !response.hasMore
            )

        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
            return MediatorResult.Error(e)
        }
    }
}