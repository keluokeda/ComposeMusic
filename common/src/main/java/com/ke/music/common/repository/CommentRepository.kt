package com.ke.music.common.repository

import androidx.paging.PagingSource
import com.ke.music.api.response.Comment
import com.ke.music.common.entity.CommentType
import com.ke.music.common.entity.IChildComment
import com.ke.music.common.entity.IComment
import kotlinx.coroutines.flow.Flow

interface CommentRepository {


    /**
     * 保存评论数据
     */
    suspend fun saveComments(
        list: List<Comment>,
        commentType: CommentType,
        sourceId: Long,
        deleteOld: Boolean,
    )


    /**
     * 获取评论数据
     */
    fun getComments(
        sourceId: Long,
        commentType: CommentType,
    ): PagingSource<Int, out IComment>


    /**
     * 切换喜欢评论状态
     * @return true表示点赞了,false表示取消点赞了
     */
    suspend fun toggleLiked(commentId: Long, isChildComment: Boolean): Boolean

    /**
     * 子评论数量增加1
     */
    suspend fun incrementChildCommentCount(commentId: Long)

    /**
     * 删除评论
     */
    suspend fun deleteComment(commentId: Long, isChildComment: Boolean)

    /**
     * 获取子评论
     * @param commentId 父评论id
     */
    fun getChildComments(commentId: Long): PagingSource<Int, out IChildComment>

    /**
     * 保存子评论
     */
    suspend fun saveChildComments(
        comments: List<Comment>,
        parentCommentId: Long,
        deleteOld: Boolean,
    )

    /**
     * 查找评论
     */
    fun findComment(commentId: Long): Flow<IComment?>
}