package com.ke.compose.music.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ke.compose.music.db.entity.Comment
import com.ke.compose.music.entity.QueryCommentAndUserResult
import com.ke.compose.music.entity.QueryCommentResult
import com.ke.compose.music.ui.comments.CommentType
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {

    @Insert(
        onConflict =
        OnConflictStrategy.REPLACE
    )
    suspend fun insertAll(comments: List<Comment>)


    //解决闪屏问题
    @Transaction
    suspend fun insertNewAndDeleteOld(comments: List<Comment>, type: CommentType, sourceId: Long) {
        deleteBySourceIdAndType(type, sourceId)
        insertAll(comments)
    }

    @Query("select * from comment")
    fun getComments(): PagingSource<Int, Comment>


    @Query("delete from comment where id = :commentId")
    suspend fun deleteById(commentId: Long)

    @Update
    suspend fun updateItem(comment: Comment)


    /**
     * 根据评论类型和资源id删除评论
     */
    @Query("delete from comment where source_id = :sourceId and type = :type")
    suspend fun deleteBySourceIdAndType(type: CommentType, sourceId: Long): Int

    /**
     * 根据评论类型和源id查询评论
     */
    @Query(
        "select comment.id as commentId,comment.time_string as timeString, user.id as userId,user.name as username,user.avatar as userAvatar,user.signature as userSignature,comment.content,comment.ip,comment.like_count as likedCount,comment.reply_count as replyCount,user_like_comment_cross_ref.created_time as likedTime " +
                "from comment inner join user on comment.user_id = user.id " +
                "left join user_like_comment_cross_ref on comment.id = user_like_comment_cross_ref.comment_id and user_like_comment_cross_ref.user_id = :currentUserId and is_child_comment = 0 " +
                "where type = :commentType and source_id = :sourceId order by comment.time desc"
    )
    fun getComments(
        commentType: CommentType,
        sourceId: Long,
        currentUserId: Long
    ): PagingSource<Int, QueryCommentResult>

    @Query("select * from comment where id = :commentId")
    suspend fun findById(commentId: Long): Comment?


    @Query("select  comment.id as commentId,content,user.name as username,user.id as userId,user.avatar as userAvatar,comment.time_string as time from comment inner join user on comment.user_id = user.id where comment.id = :commentId")
    fun queryCommentAndUser(commentId: Long): Flow<QueryCommentAndUserResult?>

    @Query("select * from comment where id = :commentId")
    suspend fun queryByCommentId(commentId: Long): Comment?
}