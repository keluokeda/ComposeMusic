package com.ke.compose.music.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ke.compose.music.db.dao.PlaylistTagDao
import com.ke.compose.music.db.entity.PlaylistTag

@Database(
    entities = [PlaylistTag::class],
    version = 1
)
abstract class AppMemoryDatabase : RoomDatabase() {


    abstract fun playlistTagDao(): PlaylistTagDao

}