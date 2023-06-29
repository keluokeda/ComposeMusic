package com.ke.compose.music.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
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
import com.ke.compose.music.db.dao.UserAlbumCrossRefDao
import com.ke.compose.music.db.dao.UserDao
import com.ke.compose.music.db.dao.UserLikeCommentCrossRefDao
import com.ke.compose.music.db.dao.UserPlaylistCrossRefDao
import com.ke.compose.music.db.entity.Album
import com.ke.compose.music.db.entity.AlbumArtistCrossRef
import com.ke.compose.music.db.entity.AlbumDetail
import com.ke.compose.music.db.entity.Artist
import com.ke.compose.music.db.entity.ChildComment
import com.ke.compose.music.db.entity.Comment
import com.ke.compose.music.db.entity.Download
import com.ke.compose.music.db.entity.Music
import com.ke.compose.music.db.entity.MusicArtistCrossRef
import com.ke.compose.music.db.entity.Playlist
import com.ke.compose.music.db.entity.PlaylistMusicCrossRef
import com.ke.compose.music.db.entity.PlaylistSubscriberCrossRef
import com.ke.compose.music.db.entity.User
import com.ke.compose.music.db.entity.UserAlbumCrossRef
import com.ke.compose.music.db.entity.UserLikeCommentCrossRef
import com.ke.compose.music.db.entity.UserPlaylistCrossRef

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
        UserAlbumCrossRef::class
//        AlbumMusicCrossRef::class
    ],
    version = 1
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
}