package com.ke.music.repository

import androidx.paging.PagingSource
import com.ke.music.api.response.Comment
import com.ke.music.common.entity.CommentType
import com.ke.music.common.entity.IComment
import com.ke.music.common.repository.CommentRepository
import com.ke.music.room.db.dao.ChildCommentDao
import com.ke.music.room.db.dao.CommentDao
import com.ke.music.room.db.dao.UserDao
import com.ke.music.room.db.dao.UserLikeCommentCrossRefDao
import com.ke.music.room.db.entity.ChildComment
import com.ke.music.room.db.entity.User
import com.ke.music.room.db.entity.UserLikeCommentCrossRef
import com.ke.music.room.entity.QueryChildCommentResult
import com.ke.music.room.entity.QueryCommentResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentRepository @Inject constructor(
    private val commentDao: CommentDao,
    private val userDao: UserDao,
    private val userLikeCommentCrossRefDao: UserLikeCommentCrossRefDao,
    private val userIdRepository: UserIdRepository,
    private val childCommentDao: ChildCommentDao,
) : CommentRepository {


    override suspend fun saveComments(
        list: List<Comment>,
        commentType: CommentType,
        sourceId: Long,
        deleteOld: Boolean,
    ) {
        insertIntoRoom(list, commentType, sourceId, deleteOld)
    }

    /**
     * 插入评论列表到数据库
     * @param deleteOld 是否删除旧的评论
     */
    private suspend fun insertIntoRoom(
        list: List<Comment>,
        commentType: CommentType,
        sourceId: Long,
        deleteOld: Boolean,
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

            com.ke.music.room.db.entity.Comment(
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

    override fun getComments(
        sourceId: Long,
        commentType: CommentType,
    ): PagingSource<Int, QueryCommentResult> {
        val userId = userIdRepository.userId

        return commentDao.getComments(commentType, sourceId, userId)
    }

    override suspend fun incrementChildCommentCount(commentId: Long) {
        val comment = commentDao.queryByCommentId(commentId) ?: return
        val result = comment.copy(
            replyCount = comment.replyCount + 1
        )
        commentDao.updateItem(result)
    }

    override suspend fun toggleLiked(commentId: Long, isChildComment: Boolean): Boolean {
        return if (isChildComment) {
            toggleLikeChildComment(commentId)
        } else {
            toggleLikeComment(commentId)
        }
    }

    /**
     * 切换喜欢评论状态
     * @return true表示点赞了,false表示取消点赞了
     */
    private suspend fun toggleLikeChildComment(commentId: Long): Boolean {
        val userId = userIdRepository.userId()
        val ref = userLikeCommentCrossRefDao.findByUserIdAndCommentId(userId, commentId, true)

        val insert = if (ref == null) {
            userLikeCommentCrossRefDao.insert(UserLikeCommentCrossRef(userId, commentId, true))
            true
        } else {
            userLikeCommentCrossRefDao.delete(ref)
            false
        }

        val comment = childCommentDao.findById(commentId)

        if (comment != null) {
            //更新新的点赞数
            childCommentDao.updateItem(
                comment.copy(
                    likedCount = comment.likedCount + if (insert) 1 else -1
                )
            )
        }

        return insert
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

    override fun findComment(commentId: Long): Flow<IComment?> =
        commentDao.queryCommentAndUser(commentId)

    suspend fun queryByCommentId(commentId: Long) = commentDao.queryByCommentId(commentId)
    suspend fun update(comment: com.ke.music.room.db.entity.Comment) =
        commentDao.updateItem(comment)

    override suspend fun deleteComment(commentId: Long, isChildComment: Boolean) {
        if (isChildComment) {
            childCommentDao.deleteById(commentId)
        } else {
            deleteById(commentId)
        }
    }

    /**
     * 删除主评论
     */
    suspend fun deleteById(commentId: Long) {
        commentDao.deleteById(commentId)
        childCommentDao.deleteByCommentId(commentId)
    }

    override fun getChildComments(
        commentId: Long,
    ): PagingSource<Int, QueryChildCommentResult> {
        val userId = userIdRepository.userId

        return childCommentDao.getChildComments(commentId, userId)
    }

    override suspend fun saveChildComments(
        comments: List<Comment>, parentCommentId: Long, deleteOld: Boolean,
    ) {


        if (deleteOld) {
            childCommentDao.deleteByCommentId(parentCommentId)
        }


        val userList = mutableListOf<User>()
        val currentUserId = userIdRepository.userId()

        val childComments = comments.map {

            if (it.liked) {
                userLikeCommentCrossRefDao.insert(
                    UserLikeCommentCrossRef(
                        currentUserId, it.commentId, true
                    )
                )
            } else {
                userLikeCommentCrossRefDao.delete(currentUserId, it.commentId, true)
            }

            userList.add(it.user.convert())


            val beRepliedUsername: String? =
                if (it.beReplied?.firstOrNull()?.beRepliedCommentId == parentCommentId) null else it.beReplied?.firstOrNull()?.user?.nickname

            ChildComment(
                it.commentId,
                parentCommentId,
                it.user.userId,
                it.content,
                it.timeString,
                it.time,
                it.likedCount,
                it.ipLocation.location,
                it.beRepliedCommentId,
                beRepliedUsername
            )
        }

        userDao.insertAll(userList)
        childCommentDao.insertAll(childComments)
    }

}