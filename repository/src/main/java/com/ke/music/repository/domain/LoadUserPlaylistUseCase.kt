package com.ke.music.repository.domain

import com.ke.music.api.HttpService
import com.ke.music.api.response.Playlist
import com.ke.music.repository.convert
import com.ke.music.room.db.dao.PlaylistDao
import com.ke.music.room.db.dao.PlaylistSubscriberCrossRefDao
import com.ke.music.room.db.dao.UserDao
import com.ke.music.room.db.dao.UserPlaylistCrossRefDao
import com.ke.music.room.db.entity.PlaylistSubscriberCrossRef
import com.ke.music.room.db.entity.User
import com.ke.music.room.db.entity.UserPlaylistCrossRef
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