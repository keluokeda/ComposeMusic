package com.ke.music.room

import android.content.Context
import androidx.room.Room
import com.ke.music.room.db.AppFileDatabase
import com.ke.music.room.db.AppMemoryDatabase
import com.ke.music.room.db.dao.AlbumArtistCrossRefDao
import com.ke.music.room.db.dao.AlbumDao
import com.ke.music.room.db.dao.AlbumDetailDao
import com.ke.music.room.db.dao.AllMvDao
import com.ke.music.room.db.dao.ArtistDao
import com.ke.music.room.db.dao.ArtistDescriptionDao
import com.ke.music.room.db.dao.ArtistMusicCrossRefDao
import com.ke.music.room.db.dao.ChildCommentDao
import com.ke.music.room.db.dao.CommentDao
import com.ke.music.room.db.dao.DownloadDao
import com.ke.music.room.db.dao.HotArtistDao
import com.ke.music.room.db.dao.LocalPlaylistSongDao
import com.ke.music.room.db.dao.MusicArtistCrossRefDao
import com.ke.music.room.db.dao.MusicDao
import com.ke.music.room.db.dao.MvArtistCrossRefDao
import com.ke.music.room.db.dao.MvDao
import com.ke.music.room.db.dao.NewAlbumDao
import com.ke.music.room.db.dao.PlaylistDao
import com.ke.music.room.db.dao.PlaylistMusicCrossRefDao
import com.ke.music.room.db.dao.PlaylistSubscriberCrossRefDao
import com.ke.music.room.db.dao.PlaylistTagDao
import com.ke.music.room.db.dao.RecommendSongDao
import com.ke.music.room.db.dao.SongLrcDao
import com.ke.music.room.db.dao.SongPlayRecordDao
import com.ke.music.room.db.dao.TopPlaylistDao
import com.ke.music.room.db.dao.UserAlbumCrossRefDao
import com.ke.music.room.db.dao.UserDao
import com.ke.music.room.db.dao.UserLikeCommentCrossRefDao
import com.ke.music.room.db.dao.UserPlaylistCrossRefDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
object RoomModule {

    @Provides
    @Singleton
    fun provideAppMemoryDatabase(
        @ApplicationContext context: Context
    ): AppMemoryDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppMemoryDatabase::class.java).build()
    }


    @Provides
    @Singleton
    fun provideAppFileDatabase(
        @ApplicationContext context: Context
    ): AppFileDatabase {
        val builder = Room.databaseBuilder(context, AppFileDatabase::class.java, "app")

//        builder.setQueryCallback(object : RoomDatabase.QueryCallback {
//            override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
//                Logger.d("$sqlQuery $bindArgs")
//            }
//
//        }, Executors.newFixedThreadPool(128))
        return builder.build()
    }


    @Provides
    @Singleton
    fun provideCommentDao(appFileDatabase: AppFileDatabase): CommentDao {
        return appFileDatabase.commentDao()
    }

    @Provides
    @Singleton
    fun provideChildCommentDao(appFileDatabase: AppFileDatabase): ChildCommentDao {
        return appFileDatabase.childCommentDao()
    }


    @Provides
    @Singleton
    fun providePlaylistTagDao(appMemoryDatabase: AppMemoryDatabase): PlaylistTagDao {
        return appMemoryDatabase.playlistTagDao()
    }


    @Provides
    @Singleton
    fun provideUserPlaylistCrossRefDao(appFileDatabase: AppFileDatabase): UserPlaylistCrossRefDao =
        appFileDatabase.userPlaylistCrossRefDao()

    @Provides
    @Singleton
    fun providePlaylistDao(appFileDatabase: AppFileDatabase): PlaylistDao =
        appFileDatabase.playlistDao()

    @Provides
    @Singleton
    fun providePlaylistSubscriberCrossRefDao(appFileDatabase: AppFileDatabase): PlaylistSubscriberCrossRefDao =
        appFileDatabase.playlistSubscriberCrossRefDao()

    @Provides
    @Singleton
    fun provideUserDao(appFileDatabase: AppFileDatabase): UserDao = appFileDatabase.userDao()

    @Provides
    @Singleton
    fun provideMusicDao(appFileDatabase: AppFileDatabase): MusicDao = appFileDatabase.musicDao()

    @Provides
    @Singleton
    fun provideAlbumDao(appFileDatabase: AppFileDatabase): AlbumDao = appFileDatabase.albumDao()

//    @Provides
//    @Singleton
//    fun provideAlbumMusicCrossRefDao(appFileDatabase: AppFileDatabase): AlbumMusicCrossRefDao =
//        appFileDatabase.albumMusicCrossRefDao()

    @Provides
    @Singleton
    fun provideMusicArtistCrossRefDao(appFileDatabase: AppFileDatabase): MusicArtistCrossRefDao =
        appFileDatabase.musicArtistCrossRefDao()

    @Provides
    @Singleton
    fun provideArtistDao(appFileDatabase: AppFileDatabase): ArtistDao = appFileDatabase.artistDao()


    @Provides
    @Singleton
    fun providePlaylistMusicCrossRefDao(appFileDatabase: AppFileDatabase): PlaylistMusicCrossRefDao {
        return appFileDatabase.playlistMusicCrossRefDao()
    }

    @Provides
    @Singleton
    fun provideDownloadDao(appFileDatabase: AppFileDatabase): DownloadDao {
        return appFileDatabase.downloadDao()
    }

    @Provides
    @Singleton
    fun provideUserLikeCommentCrossRefDao(appFileDatabase: AppFileDatabase): UserLikeCommentCrossRefDao {
        return appFileDatabase.userLikeCommentCrossRefDao()
    }

    @Provides
    @Singleton
    fun provideAlbumDetailDao(appFileDatabase: AppFileDatabase): AlbumDetailDao {
        return appFileDatabase.albumDetailDao()
    }

    @Provides
    @Singleton
    fun provideAlbumArtistCrossRefDao(appFileDatabase: AppFileDatabase): AlbumArtistCrossRefDao {
        return appFileDatabase.albumArtistCrossRefDao()
    }

    @Provides
    @Singleton
    fun provideUserAlbumCrossRefDao(appFileDatabase: AppFileDatabase): UserAlbumCrossRefDao {
        return appFileDatabase.userAlbumCrossRefDao()
    }

    @Provides
    @Singleton
    fun provideRecommendSongDao(appFileDatabase: AppFileDatabase): RecommendSongDao {
        return appFileDatabase.recommendSongDao()
    }

    @Provides
    @Singleton
    fun provideTopPlaylistDao(appFileDatabase: AppFileDatabase): TopPlaylistDao {
        return appFileDatabase.topPlaylistDao()
    }

    @Provides
    @Singleton
    fun provideNewAlbumDao(appFileDatabase: AppFileDatabase): NewAlbumDao {
        return appFileDatabase.newAlbumDao()
    }

    @Provides
    @Singleton
    fun provideHotArtistDao(appFileDatabase: AppFileDatabase): HotArtistDao {
        return appFileDatabase.hotArtistDao()
    }

    @Provides
    @Singleton
    fun provideArtistDescriptionDao(appFileDatabase: AppFileDatabase): ArtistDescriptionDao {
        return appFileDatabase.artistDescriptionDao()
    }

    @Provides
    @Singleton
    fun provideArtistMusicCrossRefDao(appFileDatabase: AppFileDatabase): ArtistMusicCrossRefDao {
        return appFileDatabase.artistMusicCrossRefDao()
    }

    @Singleton
    @Provides
    fun provideMvDao(appFileDatabase: AppFileDatabase): MvDao {
        return appFileDatabase.mvDao()
    }

    @Provides
    @Singleton
    fun provideAllMvDao(appFileDatabase: AppFileDatabase): AllMvDao {
        return appFileDatabase.allMvDao()
    }

    @Provides
    @Singleton
    fun provideMvArtistCrossRefDao(appFileDatabase: AppFileDatabase): MvArtistCrossRefDao {
        return appFileDatabase.mvArtistCrossRedDao()
    }

    @Provides
    @Singleton
    fun provideSongPlayRecordDao(appFileDatabase: AppFileDatabase): SongPlayRecordDao {
        return appFileDatabase.songPlaylistRecordDao()
    }


    @Singleton
    @Provides
    fun provideLocalPlaylistSongDao(appFileDatabase: AppFileDatabase): LocalPlaylistSongDao {
        return appFileDatabase.localPlaylistSongDao()
    }

    @Provides
    @Singleton
    fun provideSongLrcDao(appFileDatabase: AppFileDatabase): SongLrcDao {
        return appFileDatabase.songLrcDao()
    }
}