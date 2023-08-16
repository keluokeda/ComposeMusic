package com.ke.music.common.repository

import com.ke.music.api.response.Playlist
import com.ke.music.api.response.PlaylistDynamicResponse
import com.ke.music.api.response.Song
import com.ke.music.common.entity.IPlaylist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {


    /**
     * 保存当前用户的歌单
     */
    suspend fun saveCurrentUserPlaylist(list: List<Playlist>)

    /**
     * 获取当前用户的歌单
     * @param containFollowed true表示包含关注的，false就仅是自己创建的
     */
    fun getCurrentUserPlaylist(containFollowed: Boolean): Flow<List<IPlaylist>>

    /**
     * 根据id查找歌单
     */
    suspend fun findPlaylistById(id: Long): IPlaylist?

    /**
     * 当前用户删除自己的歌单
     */
    suspend fun currentUserDeleteSelfPlaylist(playlistId: Long)


    /**
     * 当前用户取消关注歌单
     */
    suspend fun currentUserCancelFollowingPlaylist(playlistId: Long)

    /**
     * 当前用户是否关注歌单
     */
    fun checkUserHasSubscribePlaylist(userId: Long, playlistId: Long): Flow<Boolean>
    fun findById(id: Long): Flow<IPlaylist?>

    /**
     * 保存歌单详情到数据库
     */
    suspend fun savePlaylistDetail(
        playlist: Playlist,
        songs: List<Song>,
        dynamic: PlaylistDynamicResponse,
    )


    /**
     * 切换关注状态
     * @return 新的关注状态
     */
    suspend fun toggleFollowPlaylist(playlistId: Long): Boolean
}