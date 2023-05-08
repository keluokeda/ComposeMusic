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
    data class NavigateToPlaylistList(val ids: LongArray) : NavigationAction {
        override fun createPath(): String {
            return Screen.PlaylistList.createPath(ids = ids)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as NavigateToPlaylistList

            if (!ids.contentEquals(other.ids)) return false

            return true
        }

        override fun hashCode(): Int {
            return ids.contentHashCode()
        }
    }
}

val LocalNavigationHandler = staticCompositionLocalOf { NavigationHandler { } }

fun interface NavigationHandler {
    fun navigate(navigationAction: NavigationAction)
}