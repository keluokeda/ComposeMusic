package com.ke.music.common.domain

import com.ke.music.api.response.User

interface GetShareUsersUseCase : IUseCase<Unit, List<User>>