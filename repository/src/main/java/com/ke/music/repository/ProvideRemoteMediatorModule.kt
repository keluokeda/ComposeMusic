package com.ke.music.repository

import androidx.lifecycle.SavedStateHandle
import com.ke.music.api.HttpService
import com.ke.music.common.entity.CommentType
import com.ke.music.common.repository.CommentRepository
import com.ke.music.common.repository.PlaylistRepository
import com.ke.music.repository.mediator.ChildCommentsRemoteMediator
import com.ke.music.repository.mediator.CommentsRemoteMediator
import com.ke.music.repository.mediator.TopPlaylistRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ProvideRemoteMediatorModule {


    @Provides
    internal fun provideCommentsRemoteMediator(
        savedStateHandle: SavedStateHandle,
        httpService: HttpService,
        commentRepository: CommentRepository,
    ): com.ke.music.common.mediator.CommentsRemoteMediator {
        val id = savedStateHandle.get<Long>("id")!!
        val commentType: CommentType = savedStateHandle.get<CommentType>("type")!!

        return CommentsRemoteMediator(httpService, commentRepository, commentType, id)
    }


    @Provides
    fun provideChildCommentsRemoteMediator(
        httpService: HttpService,
        commentRepository: CommentRepository,
        savedStateHandle: SavedStateHandle,
    ): com.ke.music.common.mediator.ChildCommentsRemoteMediator {

        val sourceId = savedStateHandle.get<Long>("id")!!
        val commentType = savedStateHandle.get<CommentType>("type")!!
        val commentId = savedStateHandle.get<Long>("commentId")!!
        return ChildCommentsRemoteMediator(
            httpService,
            commentRepository,
            commentId,
            sourceId,
            commentType
        )
    }

    @Provides
    fun provideTopPlaylistRemoteMediator(
        httpService: HttpService,
        playlistRepository: PlaylistRepository,
        savedStateHandle: SavedStateHandle,
    ): com.ke.music.common.mediator.TopPlaylistRemoteMediator {
        val category = savedStateHandle.get<String>("category") ?: "全部"

        return TopPlaylistRemoteMediator(httpService, playlistRepository, category)
    }
}