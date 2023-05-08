package com.ke.compose.music.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ke.compose.music.ui.comments.CommentType

@Dao
interface ChildCommentDao {

    @Insert(
        onConflict =
        OnConflictStrategy.REPLACE
    )
    suspend fun insertAll(comments: List<ChildComment>)

    @Query("select * from child_comment")
    fun getComments(): PagingSource<Int, ChildComment>

    /**
     * 删除所有评论
     */
    @Query("delete from child_comment")
    suspend fun deleteAll()

    @Update
    suspend fun updateItem(comment: ChildComment)
}