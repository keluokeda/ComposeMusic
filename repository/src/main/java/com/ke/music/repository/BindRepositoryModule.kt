package com.ke.music.repository

import com.ke.music.common.repository.ArtistRepository
import com.ke.music.common.repository.CurrentUserRepository
import com.ke.music.common.repository.SongRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class BindRepositoryModule {


    @Binds
    internal abstract fun provideSongRepository(musicRepository: MusicRepository): SongRepository


    @Binds
    internal abstract fun provideDownloadRepository(downloadRepository: DownloadRepository):
            com.ke.music.common.repository.DownloadRepository


    @Binds
    internal abstract fun provideAlbumRepository(albumRepository: AlbumRepository): com.ke.music.common.repository.AlbumRepository


    @Binds
    internal abstract fun provideArtistRepository(artistRepositoryImpl: ArtistRepositoryImpl): ArtistRepository


    @Binds
    internal abstract fun provideUserIdRepository(userIdRepository: UserIdRepository): CurrentUserRepository

    @Binds
    internal abstract fun provideMvRepository(mvRepository: MvRepository): com.ke.music.common.repository.MvRepository


//    @Provides
//    fun provideHotArtistRemoteMediator(hotArtistListRemoteMediator: HotArtistListRemoteMediator): HotArtistRemoteMediator {
//        return hotArtistListRemoteMediator
//    }
//
//    @Provides
//    fun provideNewAlbumsRemoteMediator(newAlbumListRemoteMediator: NewAlbumListRemoteMediator): NewAlbumsRemoteMediator {
//        return newAlbumListRemoteMediator
//    }
//
//    @Provides
//    fun provideCommentsRemoteMediator(commentsRemoteMediator: CommentsRemoteMediator): com.ke.music.common.mediator.CommentsRemoteMediator {
//        return commentsRemoteMediator
//    }
//
//    @Provides
//    fun provideCommentRepository(commentRepository: CommentRepository): com.ke.music.common.repository.CommentRepository {
//        return commentRepository
//    }
//
//    @Provides
//    fun provideChildCommentsRemoteMediator(childCommentsRemoteMediator: ChildCommentsRemoteMediator): com.ke.music.common.mediator.ChildCommentsRemoteMediator {
//        return childCommentsRemoteMediator
//    }
//
//    @Provides
//    fun providePlaylistRepository(playlistRepository: PlaylistRepository): com.ke.music.common.repository.PlaylistRepository {
//        return playlistRepository
//    }
//
//    @Provides
//    fun provideUserRepository(userRepository: UserRepository): com.ke.music.common.repository.UserRepository {
//        return userRepository
//    }
//
//    @Provides
//    fun provideTopPlaylistRemoteMediator(topPlaylistRemoteMediator: TopPlaylistRemoteMediator): com.ke.music.common.mediator.TopPlaylistRemoteMediator {
//        return topPlaylistRemoteMediator
//    }
}

