package com.ke.music.common.repository

import com.ke.music.api.response.Song

interface SongRepository {


    suspend fun saveSongToRoom(list: List<Song>)
}