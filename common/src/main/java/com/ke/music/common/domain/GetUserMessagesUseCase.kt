package com.ke.music.common.domain

import com.ke.music.api.response.PrivateMessage

interface GetUserMessagesUseCase : IUseCase<Unit, List<PrivateMessage>>