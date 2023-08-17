package com.ke.music.domain

import com.ke.music.api.HttpService
import com.ke.music.common.domain.FollowArtistUseCase
import com.ke.music.common.repository.ArtistRepository
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

internal class FollowArtistUseCaseImpl @Inject constructor(
    private val httpService: HttpService,
    private val artistRepository: ArtistRepository,
) :
    UseCase<Pair<Long, Boolean>, Unit>(Dispatchers.IO), FollowArtistUseCase {

    override suspend fun execute(parameters: Pair<Long, Boolean>) {
        artistRepository.setCurrentUserFollowArtist(parameters.first, parameters.second)
        httpService.followArtist(parameters.first, if (parameters.second) 1 else 0)
    }
}