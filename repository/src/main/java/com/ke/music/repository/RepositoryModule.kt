package com.ke.music.repository

import com.ke.music.common.mediator.HotArtistRemoteMediator
import com.ke.music.common.mediator.NewAlbumsRemoteMediator
import com.ke.music.common.repository.ArtistRepository
import com.ke.music.common.repository.CurrentUserRepository
import com.ke.music.common.repository.SongRepository
import com.ke.music.repository.mediator.AllMvRemoteMediator
import com.ke.music.repository.mediator.ChildCommentsRemoteMediator
import com.ke.music.repository.mediator.CommentsRemoteMediator
import com.ke.music.repository.mediator.HotArtistListRemoteMediator
import com.ke.music.repository.mediator.NewAlbumListRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
object RepositoryModule {

    @Provides
    @Singleton
    fun provideSongRepository(musicRepository: MusicRepository): SongRepository {
        return musicRepository
    }

    @Provides
    @Singleton
    fun provideDownloadRepository(downloadRepository: DownloadRepository): com.ke.music.common.repository.DownloadRepository {
        return downloadRepository
    }

    @Provides
    @Singleton
    fun provideAlbumRepository(albumRepository: AlbumRepository): com.ke.music.common.repository.AlbumRepository {
        return albumRepository
    }

    @Provides
    @Singleton
    fun provideArtistRepository(artistRepositoryImpl: ArtistRepositoryImpl): ArtistRepository {
        return artistRepositoryImpl
    }

    @Singleton
    @Provides
    fun provideUserIdRepository(userIdRepository: UserIdRepository): CurrentUserRepository {
        return userIdRepository
    }

    @Singleton
    @Provides
    fun provideMvRepository(mvRepository: MvRepository): com.ke.music.common.repository.MvRepository {
        return mvRepository
    }

    @Provides
    fun provideAllMvRemoteMediator(allMvRemoteMediator: AllMvRemoteMediator): com.ke.music.common.mediator.AllMvRemoteMediator {
        return allMvRemoteMediator
    }

    @Provides
    fun provideHotArtistRemoteMediator(hotArtistListRemoteMediator: HotArtistListRemoteMediator): HotArtistRemoteMediator {
        return hotArtistListRemoteMediator
    }

    @Provides
    fun provideNewAlbumsRemoteMediator(newAlbumListRemoteMediator: NewAlbumListRemoteMediator): NewAlbumsRemoteMediator {
        return newAlbumListRemoteMediator
    }

    @Provides
    fun provideCommentsRemoteMediator(commentsRemoteMediator: CommentsRemoteMediator): com.ke.music.common.mediator.CommentsRemoteMediator {
        return commentsRemoteMediator
    }

    @Provides
    fun provideCommentRepository(commentRepository: CommentRepository): com.ke.music.common.repository.CommentRepository {
        return commentRepository
    }

    @Provides
    fun provideChildCommentsRemoteMediator(childCommentsRemoteMediator: ChildCommentsRemoteMediator): com.ke.music.common.mediator.ChildCommentsRemoteMediator {
        return childCommentsRemoteMediator
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

