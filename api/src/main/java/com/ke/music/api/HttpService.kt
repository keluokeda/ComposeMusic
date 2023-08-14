package com.ke.music.api

import com.ke.music.api.response.AlbumDynamicResponse
import com.ke.music.api.response.AlbumResponse
import com.ke.music.api.response.ArtistAlbumResponse
import com.ke.music.api.response.ArtistDescResponse
import com.ke.music.api.response.ArtistListResponse
import com.ke.music.api.response.ArtistMvResponse
import com.ke.music.api.response.ArtistsResponse
import com.ke.music.api.response.CodeResponse
import com.ke.music.api.response.CommentsResponse
import com.ke.music.api.response.HighQualityPlaylistsResponse
import com.ke.music.api.response.LoginQRCreateResponse
import com.ke.music.api.response.LoginQRKeyResponse
import com.ke.music.api.response.LoginStatusResponse
import com.ke.music.api.response.LrcResponse
import com.ke.music.api.response.MusicUrlResponse
import com.ke.music.api.response.MvAllResponse
import com.ke.music.api.response.NewAlbumListResponse
import com.ke.music.api.response.PlaylistCategoryResponse
import com.ke.music.api.response.PlaylistDetailResponse
import com.ke.music.api.response.PlaylistDynamicResponse
import com.ke.music.api.response.PlaylistSubscribersResponse
import com.ke.music.api.response.PlaylistTagsResponse
import com.ke.music.api.response.PlaylistTopResponse
import com.ke.music.api.response.PlaylistTracksResponse
import com.ke.music.api.response.PrivateMessageResponse
import com.ke.music.api.response.RecommendSongsResponse
import com.ke.music.api.response.SongDetailResponse
import com.ke.music.api.response.UserDetailResponse
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
    suspend fun deletePlaylist(@Query("id") id: Long)

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
     * 删除评论
     */
    @GET("comment?t=0")
    suspend fun deleteComment(
        @Query("type") type: Int,
        @Query("id") id: Long,
        @Query("commentId") commentId: Long
    )

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
     * 专辑动态信息
     */
    @GET("album/detail/dynamic")
    suspend fun getAlbumDynamic(
        @Query("id") id: Long
    ): AlbumDynamicResponse

    /**
     * 添加或删除歌曲到歌单
     */
    @GET("playlist/tracks")
    suspend fun addOrRemoveSongsToPlaylist(
        @Query("op") option: String,
        @Query("pid") playlistId: Long,
        @Query("tracks") tracks: String
    )

    /**
     *网友精选碟
     */
    @GET("top/playlist")
    suspend fun getTopPlaylist(
        @Query("cat") category: String? = null,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PlaylistTopResponse

    /**
     * 歌单分类
     */
    @GET("playlist/catlist")
    suspend fun getPlaylistCategory(): PlaylistCategoryResponse

    /**
     * 精品歌单标签列表
     */
    @GET("playlist/highquality/tags")
    suspend fun getPlaylistTags(): PlaylistTagsResponse

    /**
     * 精品歌单列表
     */
    @GET("top/playlist/highquality")
    suspend fun getHighQualityPlaylists(
        @Query("cat") category: String?,
        @Query("limit") limit: Int = 50,
        @Query("before") before: Long? = null
    ): HighQualityPlaylistsResponse

    /**
     * 收藏或取消收藏歌单
     * @param type 1收藏 2取消收藏
     */
    @GET("playlist/subscribe")
    suspend fun subscribePlaylist(
        @Query("id") id: Long,
        @Query("t") type: Int
    ): CodeResponse

    /**
     * 获取歌曲播放地址
     */
    @GET("song/url/v1")
    suspend fun getSongUrl(
        @Query("id") id: Long,
        @Query("level") level: String = "jymaster"
    ): MusicUrlResponse

    /**
     * 获取歌曲详情
     */
    @GET("song/detail")
    suspend fun getSongDetail(
        @Query("ids") id: Long,
    ): SongDetailResponse


    /**
     * 获取歌曲详情
     */
    @GET("song/detail")
    suspend fun getSongsDetail(
        @Query("ids") ids: String,
    ): SongDetailResponse


    /**
     * 收藏或取消收藏专辑
     * @param action 1表示收藏 0表示取消收藏
     */
    @GET("album/sub")
    suspend fun collectAlbum(@Query("id") albumId: Long, @Query("t") action: Int)


    /**
     * 每日推荐
     */
    @GET("recommend/songs")
    suspend fun recommendSongs(): RecommendSongsResponse

    /**
     * 用户详情
     */
    @GET("user/detail")
    suspend fun getUserDetail(@Query("uid") userId: Long): UserDetailResponse


    /**
     * 最新专辑
     * @param area ALL:全部,ZH:华语,EA:欧美,KR:韩国,JP:日本
     */
    @GET("album/new")
    suspend fun getNewAlbumList(
        @Query("area") area: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): NewAlbumListResponse

    /**
     * 获取歌手列表
     * @param type -1全部 1男歌手 2女歌手 3乐队
     * @param area -1全部  7华语 96欧美 8日本 16韩国 0其他
     */
    @GET("artist/list")
    suspend fun getArtistList(
        @Query("type") type: Int,
        @Query("area") area: Int,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): ArtistListResponse

    /**
     * 歌手描述
     */
    @GET("artist/desc")
    suspend fun getArtistDesc(@Query("id") id: Long): ArtistDescResponse

    /**
     * 获取歌手信息和热门歌曲
     */
    @GET("artists")
    suspend fun getArtists(@Query("id") artistId: Long): ArtistsResponse

    /**
     * 获取歌手专辑
     */
    @GET("artist/album")
    suspend fun getArtistAlbums(
        @Query("id") artistId: Long,
        @Query("limit") limit: Int = 1000
    ): ArtistAlbumResponse

    /**
     * 歌手mv
     */
    @GET("/artist/mv")
    suspend fun getArtistMv(
        @Query("id") artistId: Long, @Query("limit") limit: Int = 1000
    ): ArtistMvResponse

    /**
     * 获取全部mv
     */
    @GET("mv/all")
    suspend fun getAllMv(
        @Query("type") type: String?,
        @Query("area") area: String?,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): MvAllResponse

    /**
     * 获取歌曲歌词
     */
    @GET("lyric")
    suspend fun getSongLrc(
        @Query("id") songId: Long
    ): LrcResponse
}