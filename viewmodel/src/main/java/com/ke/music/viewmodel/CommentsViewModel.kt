package com.ke.music.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import com.ke.music.common.domain.DeleteCommentUseCase
import com.ke.music.common.domain.LikeCommentUseCase
import com.ke.music.common.domain.SendCommentUseCase
import com.ke.music.common.domain.successOr
import com.ke.music.common.entity.CommentType
import com.ke.music.common.entity.DeleteCommentRequest
import com.ke.music.common.entity.IComment
import com.ke.music.common.entity.LikeCommentRequest
import com.ke.music.common.entity.SendCommentRequest
import com.ke.music.common.mediator.CommentsRemoteMediator
import com.ke.music.common.repository.CommentRepository
import com.ke.music.common.repository.CurrentUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class CommentsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val commentRepository: CommentRepository,
    private val likeCommentUseCase: LikeCommentUseCase,
    private val sendCommentUseCase: SendCommentUseCase,

    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val userIdRepository: CurrentUserRepository,
    commentsRemoteMediator: CommentsRemoteMediator,
) : ViewModel() {
    val id = savedStateHandle.get<Long>("id")!!

    val commentType: CommentType = savedStateHandle.get<CommentType>("type")!!


    private val _sending = MutableStateFlow(false)
    val sending: StateFlow<Boolean>
        get() = _sending

    private val _sendCommentResult = Channel<Boolean?>(capacity = Channel.CONFLATED)


    /**
     * 发送评论结果
     * true表示成功并且需要刷新列表 false表示成功不需要刷新列表 null表示失败
     */
    val sendCommentResult: Flow<Boolean?>
        get() = _sendCommentResult.receiveAsFlow()


    val comments: Flow<PagingData<IComment>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            initialLoadSize = 50
        ),
        remoteMediator = commentsRemoteMediator
    ) {
        commentRepository.getComments(id, commentType) as PagingSource<Int, IComment>
    }.flow
        .cachedIn(viewModelScope)


    fun toggleLiked(comment: IComment) {
        viewModelScope.launch {
            val request = LikeCommentRequest(id, commentType, comment.commentId)
            likeCommentUseCase(request)
        }
    }


    fun canDeleteComment(comment: IComment) =
        comment.user.userId == userIdRepository.userId

    /**
     * 发布评论
     */
    fun sendComment(content: String, parentCommentId: Long?) {
        viewModelScope.launch {
            _sending.value = true
            val request = SendCommentRequest(commentType, id, parentCommentId, content)
            val success = sendCommentUseCase(request).successOr(false)
            _sending.value = false
            _sendCommentResult.send(if (success) parentCommentId == null else null)
        }
    }

    fun deleteComment(commentId: Long) {
        viewModelScope.launch {
            deleteCommentUseCase(
                DeleteCommentRequest(
                    commentType, id, commentId, false
                )
            )
        }
    }
}