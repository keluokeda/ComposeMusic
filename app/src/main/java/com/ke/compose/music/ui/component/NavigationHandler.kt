package com.ke.compose.music.ui.component

import androidx.compose.runtime.staticCompositionLocalOf
import com.ke.compose.music.ui.Screen
import com.ke.compose.music.ui.comments.CommentType
import com.ke.compose.music.ui.share.ShareType

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
        val shareType: ShareType,
        val id: Long,
        val title: String,
        val subTitle: String,
        val cover: String
    ) : NavigationAction {
        override fun createPath(): String {
            return Screen.Share.createPath(shareType, id, title, subTitle, cover)
        }
    }

    /**
     * 导航到转机详情
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
    object NavigateToPlaylistList : NavigationAction {
        override fun createPath(): String {
            return Screen.PlaylistList.createPath()
        }
    }

    /**
     * 网友精选碟
     */
    object NavigateToPlaylistTop : NavigationAction {
        override fun createPath(): String {
            return Screen.PlaylistTop.route
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
}

val LocalNavigationHandler = staticCompositionLocalOf { NavigationHandler { } }

fun interface NavigationHandler {
    fun navigate(navigationAction: NavigationAction)
}