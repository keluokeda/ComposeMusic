package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ke.music.room.db.entity.User

@Dao
interface UserDao {


    /**
     * 插入用户
     */
    @Insert(
        onConflict =
        OnConflictStrategy.REPLACE
    )
    suspend fun insertAll(users: List<User>)

    /**
     * 更新用户
     */
    @Update
    suspend fun updateUser(user: User)


    @Query("select * from user where id = :id")
    suspend fun findById(id: Long): User?

}