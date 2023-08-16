package com.ke.music.domain

import com.ke.music.common.domain.CollectAlbumUseCase
import com.ke.music.common.domain.DeleteCommentUseCase
import com.ke.music.common.domain.DeletePlaylistUseCase
import com.ke.music.common.domain.FollowArtistUseCase
import com.ke.music.common.domain.FollowPlaylistUseCase
import com.ke.music.common.domain.GetPlaylistCategoryListUseCase
import com.ke.music.common.domain.GetSongLrcUseCase
import com.ke.music.common.domain.LikeCommentUseCase
import com.ke.music.common.domain.LoadAlbumDetailUseCase
import com.ke.music.common.domain.LoadArtistDetailUseCase
import com.ke.music.common.domain.LoadCurrentUserPlaylistUseCase
import com.ke.music.common.domain.LoadPlaylistDetailUseCase
import com.ke.music.common.domain.LoadRecommendSongsUseCase
import com.ke.music.common.domain.SendCommentUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
object DomainModule {

    @Provides
    fun provideGetSongLrcUseCase(getSongLrcUseCaseImpl: GetSongLrcUseCaseImpl): GetSongLrcUseCase {
        return getSongLrcUseCaseImpl
    }

    @Provides
    fun provideLoadArtistDetailUseCase(loadArtistDetailUseCaseImpl: LoadArtistDetailUseCaseImpl): LoadArtistDetailUseCase {
        return loadArtistDetailUseCaseImpl
    }

    @Provides
    fun provideFollowArtistUseCase(followArtistUseCaseImpl: FollowArtistUseCaseImpl): FollowArtistUseCase {
        return followArtistUseCaseImpl
    }

    @Provides
    fun provideLoadAlbumDetailUseCase(loadAlbumDetailUseCaseImpl: LoadAlbumDetailUseCaseImpl): LoadAlbumDetailUseCase {
        return loadAlbumDetailUseCaseImpl
    }

    @Provides
    fun provideCollectAlbumUseCase(collectAlbumUseCaseImpl: CollectAlbumUseCaseImpl): CollectAlbumUseCase {
        return collectAlbumUseCaseImpl
    }

    @Provides
    fun provideLoadRecommendSongsUseCase(loadRecommendSongsUseCaseImpl: LoadRecommendSongsUseCaseImpl): LoadRecommendSongsUseCase {
        return loadRecommendSongsUseCaseImpl
    }

    @Provides
    fun provideLikeCommentUseCase(likeCommentUseCaseImpl: LikeCommentUseCaseImpl): LikeCommentUseCase {
        return likeCommentUseCaseImpl
    }

    @Provides
    fun provideSendCommentUseCase(sendCommentUseCaseImpl: SendCommentUseCaseImpl): SendCommentUseCase {
        return sendCommentUseCaseImpl
    }

    @Provides
    fun provideDeleteCommentUseCase(deleteCommentUseCaseImpl: DeleteCommentUseCaseImpl): DeleteCommentUseCase {
        return deleteCommentUseCaseImpl
    }

    @Provides
    fun provideLoadCurrentUserPlaylistUseCase(loadCurrentUserPlaylistUseCaseImpl: LoadCurrentUserPlaylistUseCaseImpl): LoadCurrentUserPlaylistUseCase {
        return loadCurrentUserPlaylistUseCaseImpl
    }

    @Provides
    fun provideDeletePlaylistUseCase(deletePlaylistUseCaseImpl: DeletePlaylistUseCaseImpl): DeletePlaylistUseCase {
        return deletePlaylistUseCaseImpl
    }

    @Provides
    fun provideGetPlaylistCategoryListUseCase(getPlaylistCategoryListUseCaseImpl: GetPlaylistCategoryListUseCaseImpl): GetPlaylistCategoryListUseCase {
        return getPlaylistCategoryListUseCaseImpl
    }


    @Provides
    fun provideLoadPlaylistDetailUseCase(loadPlaylistDetailUseCaseImpl: LoadPlaylistDetailUseCaseImpl): LoadPlaylistDetailUseCase {
        return loadPlaylistDetailUseCaseImpl
    }

    @Provides
    fun provideFollowPlaylistUseCase(followPlaylistUseCaseImpl: FollowPlaylistUseCaseImpl): FollowPlaylistUseCase {
        return followPlaylistUseCaseImpl
    }
}