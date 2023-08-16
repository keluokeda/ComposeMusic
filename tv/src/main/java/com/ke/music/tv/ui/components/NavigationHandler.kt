package com.ke.music.tv.ui.components

import androidx.compose.runtime.staticCompositionLocalOf
import com.ke.music.common.entity.CommentType
import com.ke.music.repository.entity.ShareType
import com.ke.music.room.entity.AlbumEntity
import com.ke.music.room.entity.MusicEntity
import com.ke.music.tv.ui.Screen
import java.net.URLEncoder

sealed interface NavigationAction {

    fun createPath(): String


    /**
     * 导航到评论
     */
    data class NavigateToComments(
        val commentType: CommentType,
        val id: Long
    ) : NavigationAction {

        override fun createPath(): String {
            return Screen.Comments.createMusicComment(commentType, id)
        }
    }

    /**
     * 导航到分享
     */
    data class NavigateToShare(
//        val shareType: ShareType,
//        val id: Long,
//        val title: String,
//        val subTitle: String,
//        val cover: String
        val shareAction: ShareAction
    ) : NavigationAction {
        override fun createPath(): String {
            return Screen.Share.createPath(shareAction.createPath())
        }
    }

    /**
     * 导航到专辑详情
     */
    data class NavigateToAlbumDetail(
        val id: Long
    ) : NavigationAction {
        override fun createPath(): String {
            return Screen.AlbumDetail.createPath(id)
        }
    }

    /**
     * 导航到用户歌单列表
     */
    data class NavigateToMyPlaylist(val musicId: Long) : NavigationAction {
        override fun createPath(): String {
            return Screen.MyPlaylist.createPath(musicId)
        }
    }

    /**
     * 网友精选碟
     */
    data class NavigateToPlaylistTop(val category: String = "全部") : NavigationAction {
        override fun createPath(): String {
            return Screen.PlaylistTop.createPath(category)
        }
    }


    object NavigateToUserPlaylist : NavigationAction {
        override fun createPath(): String {
            return Screen.UserPlaylist.route
        }
    }

    /**
     * 精品歌单
     */
    object NavigateToHighqualityPlaylist : NavigationAction {
        override fun createPath(): String {
            return Screen.HighqualityPlaylist.route
        }
    }

    object NavigateToDownloadedMusic : NavigationAction {
        override fun createPath(): String {
            return Screen.DownloadedMusic.route
        }
    }


    object NavigateToDownloadingMusic : NavigationAction {
        override fun createPath(): String {
            return Screen.DownloadingMusic.route
        }
    }

    object NavigateToRecommendSongs : NavigationAction {
        override fun createPath(): String {
            return Screen.RecommendSongs.route
        }
    }

    data class NavigateToPlaylistDetail(private val id: Long) : NavigationAction {
        override fun createPath(): String {
            return Screen.PlaylistDetail.createUrl(id)
        }
    }

    object NavigateToArtistList : NavigationAction {
        override fun createPath(): String {
            return Screen.ArtistList.route
        }
    }

    object NavigateToAlbumSquare : NavigationAction {
        override fun createPath(): String {
            return Screen.AlbumSquare.route
        }
    }

    data class NavigateToArtistDetail(private val artistId: Long) : NavigationAction {

        override fun createPath(): String {
            return Screen.ArtistDetail.createPath(artistId)
        }
    }

    object NavigateToPlay : NavigationAction {
        override fun createPath(): String {
            return Screen.Play.route
        }
    }
}

val LocalNavigationHandler = staticCompositionLocalOf { NavigationHandler { } }

fun interface NavigationHandler {
    fun navigate(navigationAction: NavigationAction)
}

sealed interface ShareAction {

    companion object {
        private fun createPath(
            shareType: ShareType, id: Long, title: String, subTitle: String, cover: String
        ) = "/share?type=$shareType&id=$id&title=$title&subTitle=${
            URLEncoder.encode(
                subTitle,
                Charsets.UTF_8.name()
            )
        }&cover=$cover"
    }

    fun createPath(): String

    data class Playlist(val playlist: com.ke.music.room.db.entity.Playlist) : ShareAction {
        override fun createPath(): String {
            return Companion.createPath(
                ShareType.Playlist,
                playlist.id,
                playlist.name,
                playlist.description ?: "",
                playlist.coverImgUrl
            )
        }
    }

    data class Music(val musicEntity: MusicEntity) : ShareAction {
        override fun createPath(): String {
            return Companion.createPath(
                ShareType.Song,
                musicEntity.musicId,
                musicEntity.name,
                musicEntity.subTitle,
                musicEntity.album.imageUrl
            )
        }
    }

    data class Album(val albumEntity: AlbumEntity) : ShareAction {
        override fun createPath(): String {
            return Companion.createPath(
                ShareType.Album,
                albumEntity.albumId,
                albumEntity.name,
                albumEntity.description ?: "¬",
                albumEntity.image
            )
        }
    }
}