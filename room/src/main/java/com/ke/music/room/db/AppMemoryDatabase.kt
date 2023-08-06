package com.ke.music.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ke.music.room.db.dao.PlaylistTagDao
import com.ke.music.room.db.entity.PlaylistTag


@Database(
    entities = [PlaylistTag::class],
    version = 1
)
abstract class AppMemoryDatabase : RoomDatabase() {
    abstract fun playlistTagDao(): PlaylistTagDao

}