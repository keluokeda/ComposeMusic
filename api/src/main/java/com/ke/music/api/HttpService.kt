package com.ke.music.api

import com.ke.music.api.response.AlbumResponse
import com.ke.music.api.response.CodeResponse
import com.ke.music.api.response.CommentsResponse
import com.ke.music.api.response.LoginQRCreateResponse
import com.ke.music.api.response.LoginQRKeyResponse
import com.ke.music.api.response.LoginStatusResponse
import com.ke.music.api.response.MusicCommentsResponse
import com.ke.music.api.response.PlaylistDetailResponse
import com.ke.music.api.response.PlaylistDynamicResponse
import com.ke.music.api.response.PlaylistSubscribersResponse
import com.ke.music.api.response.PlaylistTracksResponse
import com.ke.music.api.response.PrivateMessageResponse
import com.ke.music.api.response.UserFollowsResponse
import com.ke.music.api.response.UserPlaylistResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HttpService {
    /**
     * 检查登录状态
     */
    @GET("login/status")
    suspend fun loginStatus(): LoginStatusResponse

    /**
     * 生成登录二维码key
     */
    @GET("login/qr/key")
    suspend fun createQRKey(): LoginQRKeyResponse

    /**
     * 创建二维码url
     */
    @GET("login/qr/create")
    suspend fun createQRUrl(
        @Query("key") key: String
    ): LoginQRCreateResponse

    /**
     * 检查是否登录成功
     */
    @GET("login/qr/check")
    suspend fun checkLoginByKey(
        @Query("key") key: String
    ): CodeResponse

    /**
     * 获取私信消息
     */
    @GET("msg/private")
    suspend fun getPrivateMessageList(
        @Query("limit") limit: Int = 1000
    ): PrivateMessageResponse

    /**
     * 获取用户歌单列表
     */
    @GET("user/playlist")
    suspend fun getUserPlaylistList(
        @Query("uid") userId: Long,
        @Query("limit") limit: Int = 1000
    ): UserPlaylistResponse

    /**
     * 获取歌单详情
     */
    @GET("playlist/detail")
    suspend fun getPlaylistDetail(@Query("id") id: Long): PlaylistDetailResponse

    /**
     * 获取歌单歌曲
     */
    @GET("/playlist/track/all")
    suspend fun getPlaylistTracks(@Query("id") id: Long): PlaylistTracksResponse

    /**
     * 获取歌单动态信息
     */
    @GET("playlist/detail/dynamic")
    suspend fun getPlaylistDetailDynamic(@Query("id") id: Long): PlaylistDynamicResponse


    /**
     * 创建歌单
     */
    @GET("playlist/create")
    suspend fun createPlaylist(@Query("name") name: String): CodeResponse

    /**
     * 删除歌单
     */
    @GET("playlist/delete")
    suspend fun deletePlaylist(@Query("id") id: Long): CodeResponse

    /**
     * 获取资源评论
     * @param id 资源的id
     * @param type 资源类型
     * @param sortType 排序方式  1:按推荐排序, 2:按热度排序, 3:按时间排序
     * @param cursor 当sortType为 3 时且页数不是第一页时需传入,值为上一条数据的 time
     */
    @GET("comment/new")
    suspend fun getComments(
        @Query("id") id: Long,
        @Query("type") type: Int,
        @Query("sortType") sortType: Int,
        @Query("pageSize") pageSize: Int,
        @Query("pageNo") pageNo: Int,
        @Query("cursor") cursor: Long?
    ): CommentsResponse

    /**
     * 子评论
     * @param id 资源的id
     * @param type 资源类型
     * @param parentCommentId 主评论id
     */
    @GET("comment/floor")
    suspend fun getChildComments(
        @Query("id") id: Long,
        @Query("type") type: Int,
        @Query("parentCommentId") parentCommentId: Long,
        @Query("limit") limit: Int,
        @Query("time") time: Long
    ): CommentsResponse

    /**
     * 给评论点赞
     */
    @GET("comment/like")
    suspend fun likeComment(
        @Query("id") id: Long,
        @Query("cid") commentId: Long,
        @Query("type") type: Int,
        @Query("t") like: Int
    ): CodeResponse

    /**
     * 发送评论
     * @param action 1表示发送 2表示回复
     * @param type
     * @param id 对应资源id
     * @param content 回复的内容
     * @param commentId 如果是回复评论，需要传入被评论的id
     */
    @GET("comment")
    suspend fun sendComment(
        @Query("t") action: Int,
        @Query("type") type: Int,
        @Query("id") id: Long,
        @Query("content") content: String,
        @Query("commentId") commentId: Long? = null
    ): CodeResponse

    /**
     * 获取某个用户关注的人
     */
    @GET("user/follows")
    suspend fun getUserFollows(
        @Query("uid") userId: Long,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): UserFollowsResponse

    /***
     * 发送歌单给用户
     */
    @GET("send/playlist")
    suspend fun sendPlaylistToUsers(
        @Query("user_ids") users: String,
        @Query("playlist") playlistId: Long,
        @Query("msg") content: String
    ): CodeResponse

    /**
     * 分享专辑给用户
     */
    @GET("send/album")
    suspend fun sendAlbumToUsers(
        @Query("user_ids") users: String,
        @Query("id") albumId: Long,
        @Query("msg") content: String
    ): CodeResponse

    /**
     * 分享歌曲给用户
     */
    @GET("send/song")
    suspend fun sendSongToUsers(
        @Query("user_ids") users: String,
        @Query("id") albumId: Long,
        @Query("msg") content: String
    ): CodeResponse

    /**
     * 歌单订阅者
     */
    @GET("playlist/subscribers")
    suspend fun playlistSubscribers(
        @Query("id") id: Long,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): PlaylistSubscribersResponse

    /**
     * 关注用户
     * @param action 1表示关注 2表示取关
     */
    @GET("follow")
    suspend fun followUser(
        @Query("id") id: Long,
        @Query("t") action: Int
    ): CodeResponse

    /**
     * 获取专辑信息
     */
    @GET("album")
    suspend fun getAlbumDetail(
        @Query("id") id: Long
    ): AlbumResponse

    /**
     * 添加或删除歌曲到歌单
     */
    @GET("playlist/tracks")
    suspend fun addOrRemoveSongsToPlaylist(
        @Query("op") option: String,
        @Query("pid") playlistId: Long,
        @Query("tracks") tracks: String
    ): CodeResponse
}