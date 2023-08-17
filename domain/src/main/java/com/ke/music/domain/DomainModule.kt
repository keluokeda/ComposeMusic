package com.ke.music.domain

import com.ke.music.common.domain.AddOrRemoveSongsToPlaylistUseCase
import com.ke.music.common.domain.CollectAlbumUseCase
import com.ke.music.common.domain.CreatePlaylistUseCase
import com.ke.music.common.domain.CreateQRUrlUseCase
import com.ke.music.common.domain.DeleteCommentUseCase
import com.ke.music.common.domain.DeletePlaylistUseCase
import com.ke.music.common.domain.DownloadSongListUseCase
import com.ke.music.common.domain.FollowArtistUseCase
import com.ke.music.common.domain.FollowPlaylistUseCase
import com.ke.music.common.domain.GetPlaylistCategoryListUseCase
import com.ke.music.common.domain.GetShareUsersUseCase
import com.ke.music.common.domain.GetSongLrcUseCase
import com.ke.music.common.domain.GetSongUrlUseCase
import com.ke.music.common.domain.LikeCommentUseCase
import com.ke.music.common.domain.LoadAlbumDetailUseCase
import com.ke.music.common.domain.LoadArtistDetailUseCase
import com.ke.music.common.domain.LoadCurrentUserPlaylistUseCase
import com.ke.music.common.domain.LoadPlaylistDetailUseCase
import com.ke.music.common.domain.LoadRecommendSongsUseCase
import com.ke.music.common.domain.LoginUseCase
import com.ke.music.common.domain.SendCommentUseCase
import com.ke.music.common.domain.ShareResourceUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class DomainModule {

    @Binds
    internal abstract fun bindGetSongLrcUseCase(getSongLrcUseCaseImpl: GetSongLrcUseCaseImpl): GetSongLrcUseCase

    @Binds
    internal abstract fun bindLoadArtistDetailUseCase(loadArtistDetailUseCaseImpl: LoadArtistDetailUseCaseImpl): LoadArtistDetailUseCase

    @Binds
    internal abstract fun bindFollowArtistUseCase(followArtistUseCaseImpl: FollowArtistUseCaseImpl): FollowArtistUseCase

    @Binds
    internal abstract fun bindLoadAlbumDetailUseCase(loadAlbumDetailUseCaseImpl: LoadAlbumDetailUseCaseImpl): LoadAlbumDetailUseCase

    @Binds
    internal abstract fun bindCollectAlbumUseCase(collectAlbumUseCaseImpl: CollectAlbumUseCaseImpl): CollectAlbumUseCase

    @Binds
    internal abstract fun bindLoadRecommendSongsUseCase(loadRecommendSongsUseCaseImpl: LoadRecommendSongsUseCaseImpl): LoadRecommendSongsUseCase

    @Binds
    internal abstract fun bindLikeCommentUseCase(likeCommentUseCaseImpl: LikeCommentUseCaseImpl): LikeCommentUseCase

    @Binds
    internal abstract fun bindSendCommentUseCase(sendCommentUseCaseImpl: SendCommentUseCaseImpl): SendCommentUseCase

    @Binds
    internal abstract fun bindDeleteCommentUseCase(deleteCommentUseCaseImpl: DeleteCommentUseCaseImpl): DeleteCommentUseCase

    @Binds
    internal abstract fun bindLoadCurrentUserPlaylistUseCase(loadCurrentUserPlaylistUseCaseImpl: LoadCurrentUserPlaylistUseCaseImpl):
            LoadCurrentUserPlaylistUseCase

    @Binds
    internal abstract fun bindDeletePlaylistUseCase(deletePlaylistUseCaseImpl: DeletePlaylistUseCaseImpl): DeletePlaylistUseCase

    @Binds
    internal abstract fun bindGetPlaylistCategoryListUseCase(getPlaylistCategoryListUseCaseImpl: GetPlaylistCategoryListUseCaseImpl):
            GetPlaylistCategoryListUseCase


    @Binds
    internal abstract fun bindLoadPlaylistDetailUseCase(loadPlaylistDetailUseCaseImpl: LoadPlaylistDetailUseCaseImpl):
            LoadPlaylistDetailUseCase

    @Binds
    internal abstract fun bindFollowPlaylistUseCase(followPlaylistUseCaseImpl: FollowPlaylistUseCaseImpl): FollowPlaylistUseCase

    @Binds
    internal abstract fun bindCreatePlaylistUseCase(createPlaylistUseCaseImpl: CreatePlaylistUseCaseImpl): CreatePlaylistUseCase

    @Binds
    internal abstract fun bindsGetShareUsersUseCase(getShareUsersUseCaseImpl: GetShareUsersUseCaseImpl): GetShareUsersUseCase

    @Binds
    internal abstract fun bindShareResourceUseCase(shareResourceUseCaseImpl: ShareResourceUseCaseImpl): ShareResourceUseCase

    @Binds
    internal abstract fun bindDownloadSongListUseCase(downloadSongListUseCaseImpl: DownloadSongListUseCaseImpl): DownloadSongListUseCase

    @Binds
    internal abstract fun bindGetSongUrlUseCase(getSongUrlUseCaseImpl: GetSongUrlUseCaseImpl): GetSongUrlUseCase

    @Binds
    internal abstract fun bindCreateQRUrlUseCase(createQRUrlUseCaseImpl: CreateQRUrlUseCaseImpl): CreateQRUrlUseCase

    @Binds
    internal abstract fun bindLoginUseCase(loginUseCaseImpl: LoginUseCaseImpl): LoginUseCase

    @Binds
    internal abstract fun bindAddOrRemoveSongsToPlaylistUseCase(
        addOrRemoveSongsToPlaylistUseCaseImpl: AddOrRemoveSongsToPlaylistUseCaseImpl,
    ): AddOrRemoveSongsToPlaylistUseCase
}