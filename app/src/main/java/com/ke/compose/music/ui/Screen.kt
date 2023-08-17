package com.ke.compose.music.ui

import com.ke.music.common.entity.CommentType
import com.ke.music.common.entity.IPlaylist
import com.ke.music.common.entity.ShareType
import com.ke.music.repository.entity.UsersType
import java.net.URLEncoder

sealed class Screen(val route: String) {
    object Splash : Screen("/app/splash")

    object Login : Screen("/app/login")

    object Main : Screen("/app/main")

    /**
     * 已下载的音乐
     */
    object DownloadedMusic : Screen("/downloaded/music")

    /**
     * 下载中的音乐
     */
    object DownloadingMusic : Screen("/downloading/music")

    /**
     * 歌单详情
     */
    object PlaylistDetail : Screen("/playlist/detail/{id}") {
        fun createUrl(playlist: IPlaylist) = "/playlist/detail/${playlist.id}"
    }

    /**
     * 新建歌单
     */
    object PlaylistNew : Screen("/playlist/new")

    /**
     * 评论
     */
    object Comments : Screen("/comments/{type}/{id}") {

        fun createMusicComment(type: CommentType, id: Long) = "/comments/${type}/$id"
    }

    /**
     * 子评论
     */
    object ChildComment : Screen("/comments/child?id={id}&type={type}&commentId={commentId}") {
        fun createPath(type: CommentType, id: Long, commentId: Long) =
            "/comments/child?id=$id&type=${type}&commentId=$commentId"
    }

    /**
     * 歌单信息
     */
    object PlaylistInfo :
        Screen("/playlist/info?name={name}&description={description}&image={image}") {

        fun createFromPlaylist(playlist: IPlaylist) =
            "/playlist/info?name=${playlist.name}&description=${
                URLEncoder.encode(
                    playlist.description ?: "",
                    Charsets.UTF_8.name()
                )
            }&image=${playlist.coverImgUrl}"
    }

    /**
     * 分享
     */
    object Share :
        Screen("/share?type={type}&id={id}&title={title}&subTitle={subTitle}&cover={cover}") {

        fun createPath(
            shareType: ShareType,
            id: Long,
            title: String,
            subTitle: String,
            cover: String,
        ): String {
            return "/share?type=$shareType&id=$id&title=$title&subTitle=${
                URLEncoder.encode(
                    subTitle,
                    Charsets.UTF_8.name()
                )
            }&cover=$cover"
        }
    }

    /**
     * 用户列表
     */
    object Users : Screen("/users?title={title}&id={id}&type={type}") {

        fun createPath(title: String, sourceId: Long, usersType: UsersType): String {
            return "/users?title=$title&id=$sourceId&type=$usersType"
        }
    }

    /**
     * 专辑详情
     */
    object AlbumDetail : Screen("/album/{id}") {
        fun createPath(id: Long) = "/album/$id"
    }

    /**
     * 我的歌单列表
     */
    object PlaylistList : Screen("/playlist/list") {

        fun createPath() = "/playlist/list"

    }

    /**
     * 网友精选碟
     */
    object PlaylistTop : Screen("/playlist/top/{category}") {
        fun createPath(category: String = "全部") = "/playlist/top/$category"

    }

    /**
     * 歌单分类
     */
    object PlaylistCategory : Screen("/playlist/category")

    /**
     * 精品歌单
     */
    object HighqualityPlaylist : Screen("/playlist/highquality")

    /**
     * 每日推荐
     */
    object RecommendSongs : Screen("/recommend/songs")

    /**
     * 专辑广场
     */
    object AlbumSquare : Screen("/album/square")

    /**
     * 歌手列表
     */
    object ArtistList : Screen("/artist/list")

    /**
     * 歌手详情
     */
    object ArtistDetail : Screen("/artist/{id}") {

        fun createPath(artistId: Long) = "/artist/$artistId"
    }

    object AllMv : Screen("/mv/all")

    object Play : Screen("/play")
}