package com.ke.music.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
object ProvideRepositoryModule {


    @Provides
    fun provideCommentRepository(commentRepository: CommentRepository): com.ke.music.common.repository.CommentRepository {
        return commentRepository
    }


    @Provides
    fun providePlaylistRepository(playlistRepository: PlaylistRepository): com.ke.music.common.repository.PlaylistRepository {
        return playlistRepository
    }

    @Provides
    fun provideUserRepository(userRepository: UserRepository): com.ke.music.common.repository.UserRepository {
        return userRepository
    }


}

