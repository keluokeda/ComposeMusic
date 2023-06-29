package com.ke.compose.music.ui.playlist

import com.ke.compose.music.convert
import com.ke.compose.music.db.dao.PlaylistDao
import com.ke.compose.music.db.dao.PlaylistSubscriberCrossRefDao
import com.ke.compose.music.db.dao.UserDao
import com.ke.compose.music.db.dao.UserPlaylistCrossRefDao
import com.ke.compose.music.db.entity.PlaylistSubscriberCrossRef
import com.ke.compose.music.db.entity.User
import com.ke.compose.music.db.entity.UserPlaylistCrossRef
import com.ke.compose.music.domain.UseCase
import com.ke.music.api.HttpService
import com.ke.music.api.response.Playlist
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class LoadUserPlaylistUseCase @Inject constructor(
    private val httpService: HttpService,
    private val userPlaylistCrossRefDao: UserPlaylistCrossRefDao,
    private val playlistSubscriberCrossRefDao: PlaylistSubscriberCrossRefDao,
    private val playlistDao: PlaylistDao,
    private val userDao: UserDao
) :
    UseCase<Long, Unit>(Dispatchers.IO) {
    override suspend fun execute(parameters: Long) {
        val list = httpService.getUserPlaylistList(parameters).playlist

        val userPlaylist = mutableListOf<Playlist>()
        val userFollowingPlaylist = mutableListOf<Playlist>()
        val userList = mutableListOf<User>()

        list.forEach { playlist ->
            userList.add(playlist.creator.convert())
            if (playlist.creator.userId == parameters) {
                //用户创建的
                userPlaylist.add(playlist)
            } else {
                //用户订阅的
                userFollowingPlaylist.add(playlist)
            }
//            userList.addAll(playlist.subscribers.map {
//                it.convert()
//            })
        }


        playlistDao.insertAll(
            list.map { it.convert() }
        )

        userDao.insertAll(userList)

        userPlaylistCrossRefDao.insertAll(userPlaylist.mapIndexed { index, playlist ->
            UserPlaylistCrossRef(parameters, playlist.id, index)
        })

        playlistSubscriberCrossRefDao.insertAll(
            userFollowingPlaylist.mapIndexed { index, playlist ->
                PlaylistSubscriberCrossRef(playlist.id, parameters, index)
            }

        )
    }
}