package com.ke.compose.music.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ke.compose.music.ui.users.UsersType

@Dao
interface UserDao {

    /**
     * 删除用户
     */
    @Query("delete from user where users_type = :usersType and source_id = :sourceId")
    suspend fun deleteByTypeAndSourceId(usersType: UsersType, sourceId: Long)


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

    /**
     * 获取用户列表
     */
    @Query("select * from user where users_type = :usersType and source_id = :sourceId")
    fun getUsers(usersType: UsersType, sourceId: Long): PagingSource<Int, User>
}