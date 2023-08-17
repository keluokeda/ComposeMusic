package com.ke.music.repository

import androidx.paging.PagingSource
import com.ke.music.api.response.Comment
import com.ke.music.common.repository.CurrentUserRepository
import com.ke.music.room.db.dao.ChildCommentDao
import com.ke.music.room.db.dao.UserDao
import com.ke.music.room.db.dao.UserLikeCommentCrossRefDao
import com.ke.music.room.db.entity.ChildComment
import com.ke.music.room.db.entity.User
import com.ke.music.room.db.entity.UserLikeCommentCrossRef
import com.ke.music.room.entity.QueryChildCommentResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChildCommentRepository @Inject constructor(
    private val childCommentDao: ChildCommentDao,
    private val userLikeCommentCrossRefDao: UserLikeCommentCrossRefDao,
    private val userIdRepository: CurrentUserRepository,
    private val userDao: UserDao,
) {

    fun getChildComments(
        commentId: Long,
    ): PagingSource<Int, QueryChildCommentResult> {
        val userId = userIdRepository.userId

        return childCommentDao.getChildComments(commentId, userId)
    }


    /**
     * 保存子评论列表到数据库
     */
    suspend fun saveChildCommentToRoom(
        comments: List<Comment>, parentCommentId: Long, deleteOld: Boolean
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


    /**
     * 切换喜欢评论状态
     * @return true表示点赞了,false表示取消点赞了
     */
    suspend fun toggleLikeComment(commentId: Long): Boolean {
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


    suspend fun deleteComment(commentId: Long) {
        childCommentDao.deleteById(commentId)
    }
}

