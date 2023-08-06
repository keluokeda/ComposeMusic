package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ke.music.room.db.entity.UserPlaylistCrossRef

@Dao
interface UserPlaylistCrossRefDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<UserPlaylistCrossRef>)


}
