package com.ke.music.room.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ke.music.room.db.entity.UserLikeCommentCrossRef

@Dao
interface UserLikeCommentCrossRefDao {

    /**
     * 插入一条记录
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userLikeCommentCrossRef: UserLikeCommentCrossRef)

    /**
     * 删除一条记录
     */
    @Query("delete from user_like_comment_cross_ref where user_id = :userId and comment_id = :commentId and is_child_comment = :isChildComment")
    suspend fun delete(userId: Long, commentId: Long, isChildComment: Boolean): Int

    /**
     * 删除一条记录
     */
    @Delete
    suspend fun delete(userLikeCommentCrossRef: UserLikeCommentCrossRef)

    @Query("select * from user_like_comment_cross_ref where user_id = :userId and comment_id = :commentId and is_child_comment = :isChildComment")
    suspend fun findByUserIdAndCommentId(
        userId: Long,
        commentId: Long,
        isChildComment: Boolean
    ): UserLikeCommentCrossRef?
}