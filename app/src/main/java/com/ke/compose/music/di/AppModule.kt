package com.ke.compose.music.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.ke.compose.music.db.AppFileDatabase
import com.ke.compose.music.db.AppMemoryDatabase
import com.ke.compose.music.db.dao.AlbumArtistCrossRefDao
import com.ke.compose.music.db.dao.AlbumDao
import com.ke.compose.music.db.dao.AlbumDetailDao
import com.ke.compose.music.db.dao.ArtistDao
import com.ke.compose.music.db.dao.ChildCommentDao
import com.ke.compose.music.db.dao.CommentDao
import com.ke.compose.music.db.dao.DownloadDao
import com.ke.compose.music.db.dao.MusicArtistCrossRefDao
import com.ke.compose.music.db.dao.MusicDao
import com.ke.compose.music.db.dao.PlaylistDao
import com.ke.compose.music.db.dao.PlaylistMusicCrossRefDao
import com.ke.compose.music.db.dao.PlaylistSubscriberCrossRefDao
import com.ke.compose.music.db.dao.PlaylistTagDao
import com.ke.compose.music.db.dao.UserAlbumCrossRefDao
import com.ke.compose.music.db.dao.UserDao
import com.ke.compose.music.db.dao.UserLikeCommentCrossRefDao
import com.ke.compose.music.db.dao.UserPlaylistCrossRefDao
import com.ke.music.api.HttpService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val logger = HttpLoggingInterceptor {
            Log.d("Http", it)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addNetworkInterceptor(logger)
            .cookieJar(
                PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
            )
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .url(
                        original.url.newBuilder()
                            .addQueryParameter("timestamp", System.currentTimeMillis().toString())
                            .build()
                    )
                    .build()

                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideHttpService(okHttpClient: OkHttpClient): HttpService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl("https://ke-music.cpolar.top/")
            .build()
            .create(HttpService::class.java)
    }

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
}