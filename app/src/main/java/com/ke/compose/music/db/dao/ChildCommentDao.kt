package com.ke.compose.music.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ke.compose.music.db.entity.ChildComment
import com.ke.compose.music.entity.QueryChildCommentResult
import kotlinx.coroutines.flow.Flow

@Dao
interface ChildCommentDao {

    @Insert(
        onConflict =
        OnConflictStrategy.REPLACE
    )
    suspend fun insertAll(comments: List<ChildComment>)


//    fun getChildComments(parentCommentId: Long):PagingSource<Int,ChildComment>

    @Query("select * from child_comment where comment_id = :parentCommentId")
    fun getAllByParentCommentId(parentCommentId: Long): Flow<List<ChildComment>>

    @Update
    suspend fun updateItem(comment: ChildComment)


    /**
     * 查询主评论的子评论
     */
    @Query(
        "select child_comment.id as commentId, child_comment.comment_id as parentCommentId , child_comment.be_replied_comment_id as beRepliedCommentId,be_replied_username as beRepliedUsername,child_comment.time_string as timeString, user.id as userId,user.name as username,user.avatar as userAvatar,user.signature as userSignature,child_comment.content,child_comment.ip,child_comment.like_count as likedCount,user_like_comment_cross_ref.created_time as likedTime " +
                "from child_comment inner join user on child_comment.user_id = user.id " +
                "left join user_like_comment_cross_ref on child_comment.id = user_like_comment_cross_ref.comment_id and user_like_comment_cross_ref.user_id = :currentUserId and is_child_comment = 1 " +
                "where child_comment.comment_id = :parentCommentId order by child_comment.time desc"
    )
    fun getChildComments(
        parentCommentId: Long,
        currentUserId: Long
    ): PagingSource<Int, QueryChildCommentResult>

    @Query("delete from child_comment where comment_id = :parentCommentId")
    suspend fun deleteByCommentId(parentCommentId: Long)

    @Query("select * from child_comment where id = :commentId")
    suspend fun findById(commentId: Long): ChildComment?

    @Query("delete from child_comment where id = :commentId")
    suspend fun deleteById(commentId: Long)

}