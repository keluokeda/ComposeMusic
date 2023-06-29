package com.ke.compose.music.repository

import androidx.paging.PagingSource
import com.ke.compose.music.convert
import com.ke.compose.music.db.dao.ChildCommentDao
import com.ke.compose.music.db.dao.CommentDao
import com.ke.compose.music.db.dao.UserDao
import com.ke.compose.music.db.dao.UserLikeCommentCrossRefDao
import com.ke.compose.music.db.entity.User
import com.ke.compose.music.db.entity.UserLikeCommentCrossRef
import com.ke.compose.music.entity.QueryCommentResult
import com.ke.compose.music.ui.comments.CommentType
import com.ke.music.api.response.Comment
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val commentDao: CommentDao,
    private val userDao: UserDao,
    private val userLikeCommentCrossRefDao: UserLikeCommentCrossRefDao,
    private val userIdRepository: UserIdRepository,
    private val childCommentDao: ChildCommentDao
) {

    /**
     * 插入评论列表到数据库
     * @param deleteOld 是否删除旧的评论
     */
    suspend fun insertIntoRoom(
        list: List<Comment>,
        commentType: CommentType,
        sourceId: Long,
        deleteOld: Boolean
    ) {


        val currentUserId = userIdRepository.userId()

//        if (deleteOld) {
//            val count = commentDao.deleteBySourceIdAndType(commentType, sourceId)
//            Logger.d("删除旧的评论 $count")
//        }

        val userList = mutableListOf<User>()


        val commentList = list.map { comment ->
            userList.add(comment.user.convert())

            if (comment.liked) {
                //插入记录
                userLikeCommentCrossRefDao.insert(
                    UserLikeCommentCrossRef(
                        currentUserId,
                        comment.commentId,
                        false
                    )
                )
            } else {
                //删除记录
                userLikeCommentCrossRefDao.delete(currentUserId, comment.commentId, false)
            }

            com.ke.compose.music.db.entity.Comment(
                comment.commentId,
                comment.user.userId,
                comment.content ?: "",
                comment.ipLocation.location,
                comment.timeString,
                comment.time,
                comment.likedCount,
                comment.replyCount,
                commentType, sourceId
            )

        }

        userDao.insertAll(userList)

        if (deleteOld) {
            commentDao.insertNewAndDeleteOld(commentList, commentType, sourceId)
        } else {
            commentDao.insertAll(commentList)
        }


    }

    fun getComments(
        sourceId: Long,
        commentType: CommentType
    ): PagingSource<Int, QueryCommentResult> {
        val userId = userIdRepository.userId

        return commentDao.getComments(commentType, sourceId, userId)
    }

    /**
     * 切换喜欢评论状态
     * @return true表示点赞了,false表示取消点赞了
     */
    suspend fun toggleLikeComment(commentId: Long): Boolean {
        val userId = userIdRepository.userId()
        val ref = userLikeCommentCrossRefDao.findByUserIdAndCommentId(userId, commentId, false)

        val insert = if (ref == null) {
            userLikeCommentCrossRefDao.insert(UserLikeCommentCrossRef(userId, commentId, false))
            true
        } else {
            userLikeCommentCrossRefDao.delete(ref)
            false
        }

        val comment = commentDao.findById(commentId)

        if (comment != null) {
            //更新新的点赞数
            commentDao.updateItem(
                comment.copy(
                    likedCount = comment.likedCount + if (insert) 1 else -1
                )
            )
        }

        return insert
    }

    fun queryCommentAndUser(commentId: Long) = commentDao.queryCommentAndUser(commentId)
    suspend fun queryByCommentId(commentId: Long) = commentDao.queryByCommentId(commentId)
    suspend fun update(comment: com.ke.compose.music.db.entity.Comment) =
        commentDao.updateItem(comment)

    /**
     * 删除主评论
     */
    suspend fun deleteById(commentId: Long) {
        commentDao.deleteById(commentId)
        childCommentDao.deleteByCommentId(commentId)
    }
}