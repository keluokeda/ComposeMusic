package com.ke.music.room.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
import com.ke.music.room.db.dao.MusicArtistCrossRefDao
import com.ke.music.room.db.dao.MusicDao
import com.ke.music.room.db.dao.MvArtistCrossRefDao
import com.ke.music.room.db.dao.MvDao
import com.ke.music.room.db.dao.NewAlbumDao
import com.ke.music.room.db.dao.PlaylistDao
import com.ke.music.room.db.dao.PlaylistMusicCrossRefDao
import com.ke.music.room.db.dao.PlaylistSubscriberCrossRefDao
import com.ke.music.room.db.dao.RecommendSongDao
import com.ke.music.room.db.dao.TopPlaylistDao
import com.ke.music.room.db.dao.UserAlbumCrossRefDao
import com.ke.music.room.db.dao.UserDao
import com.ke.music.room.db.dao.UserLikeCommentCrossRefDao
import com.ke.music.room.db.dao.UserPlaylistCrossRefDao
import com.ke.music.room.db.entity.Album
import com.ke.music.room.db.entity.AlbumArtistCrossRef
import com.ke.music.room.db.entity.AlbumDetail
import com.ke.music.room.db.entity.AllMv
import com.ke.music.room.db.entity.Artist
import com.ke.music.room.db.entity.ArtistDescription
import com.ke.music.room.db.entity.ArtistMusicCrossRef
import com.ke.music.room.db.entity.ChildComment
import com.ke.music.room.db.entity.Comment
import com.ke.music.room.db.entity.Download
import com.ke.music.room.db.entity.HotArtist
import com.ke.music.room.db.entity.Music
import com.ke.music.room.db.entity.MusicArtistCrossRef
import com.ke.music.room.db.entity.Mv
import com.ke.music.room.db.entity.MvArtistCrossRef
import com.ke.music.room.db.entity.NewAlbum
import com.ke.music.room.db.entity.Playlist
import com.ke.music.room.db.entity.PlaylistMusicCrossRef
import com.ke.music.room.db.entity.PlaylistSubscriberCrossRef
import com.ke.music.room.db.entity.RecommendSong
import com.ke.music.room.db.entity.TopPlaylist
import com.ke.music.room.db.entity.User
import com.ke.music.room.db.entity.UserAlbumCrossRef
import com.ke.music.room.db.entity.UserLikeCommentCrossRef
import com.ke.music.room.db.entity.UserPlaylistCrossRef


@Database(
    entities = [
        Comment::class,
        ChildComment::class,
        User::class,
        UserPlaylistCrossRef::class,
        PlaylistSubscriberCrossRef::class,
        Playlist::class,
        Music::class,
        Album::class,
        AlbumDetail::class,
        AlbumArtistCrossRef::class,
        Artist::class,
        MusicArtistCrossRef::class,
        PlaylistMusicCrossRef::class,
        Download::class,
        UserLikeCommentCrossRef::class,
        UserAlbumCrossRef::class,
        RecommendSong::class,
        TopPlaylist::class,
        NewAlbum::class,
        HotArtist::class,
        ArtistDescription::class,
        ArtistMusicCrossRef::class,
        Mv::class,
        MvArtistCrossRef::class,
        AllMv::class
    ],
    version = 8,
    autoMigrations = [
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
    ],

    exportSchema = true

)
@TypeConverters(Converts::class)
abstract class AppFileDatabase : RoomDatabase() {


    abstract fun userPlaylistCrossRefDao(): UserPlaylistCrossRefDao

    abstract fun playlistSubscriberCrossRefDao(): PlaylistSubscriberCrossRefDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun userDao(): UserDao

    abstract fun musicDao(): MusicDao

    abstract fun albumDao(): AlbumDao

    abstract fun artistDao(): ArtistDao

    abstract fun musicArtistCrossRefDao(): MusicArtistCrossRefDao

    abstract fun playlistMusicCrossRefDao(): PlaylistMusicCrossRefDao

    abstract fun downloadDao(): DownloadDao

    abstract fun userLikeCommentCrossRefDao(): UserLikeCommentCrossRefDao

    abstract fun commentDao(): CommentDao

    abstract fun childCommentDao(): ChildCommentDao

    abstract fun albumDetailDao(): AlbumDetailDao

    abstract fun albumArtistCrossRefDao(): AlbumArtistCrossRefDao

    abstract fun userAlbumCrossRefDao(): UserAlbumCrossRefDao

    abstract fun recommendSongDao(): RecommendSongDao

    abstract fun topPlaylistDao(): TopPlaylistDao

    abstract fun newAlbumDao(): NewAlbumDao

    abstract fun hotArtistDao(): HotArtistDao

    abstract fun artistDescriptionDao(): ArtistDescriptionDao

    abstract fun artistMusicCrossRefDao(): ArtistMusicCrossRefDao

    abstract fun mvDao(): MvDao

    abstract fun mvArtistCrossRedDao(): MvArtistCrossRefDao

    abstract fun allMvDao(): AllMvDao
}