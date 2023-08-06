package com.ke.compose.music.ui.comments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ke.music.api.HttpService
import com.ke.music.repository.CommentRepository
import com.ke.music.repository.UserIdRepository
import com.ke.music.repository.domain.DeleteCommentRequest
import com.ke.music.repository.domain.DeleteCommentUseCase
import com.ke.music.repository.domain.LikeCommentRequest
import com.ke.music.repository.domain.LikeCommentUseCase
import com.ke.music.repository.domain.SendCommentRequest
import com.ke.music.repository.domain.SendCommentUseCase
import com.ke.music.repository.domain.successOr
import com.ke.music.repository.mediator.CommentsRemoteMediator
import com.ke.music.room.entity.CommentType
import com.ke.music.room.entity.QueryCommentResult
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
internal class CommentsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    httpService: HttpService,
    private val commentRepository: CommentRepository,
    private val likeCommentUseCase: LikeCommentUseCase,
    private val sendCommentUseCase: SendCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val userIdRepository: UserIdRepository
) : ViewModel() {
    internal val id = savedStateHandle.get<Long>("id")!!

    internal val commentType: CommentType = savedStateHandle.get<CommentType>("type")!!


    private val _sending = MutableStateFlow(false)
    internal val sending: StateFlow<Boolean>
        get() = _sending

    private val _sendCommentResult = Channel<Boolean?>(capacity = Channel.CONFLATED)

    /**
     * 发送评论结果
     * true表示成功并且需要刷新列表 false表示成功不需要刷新列表 null表示失败
     */
    internal val sendCommentResult: Flow<Boolean?>
        get() = _sendCommentResult.receiveAsFlow()

    private val remoteMediator = CommentsRemoteMediator(
        httpService,
        id,
        commentType,
        commentRepository
    )

    val comments: Flow<PagingData<QueryCommentResult>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            initialLoadSize = 50
        ),
        remoteMediator = remoteMediator
    ) {
        commentRepository.getComments(id, commentType)
    }.flow
        .cachedIn(viewModelScope)

    internal fun toggleLiked(comment: QueryCommentResult) {
        viewModelScope.launch {
            val request = LikeCommentRequest(id, commentType, comment.commentId, !comment.liked)
            likeCommentUseCase(request)
        }
    }


    fun canDeleteComment(queryCommentResult: QueryCommentResult) =
        queryCommentResult.userId == userIdRepository.userId

    /**
     * 发布评论
     */
    internal fun sendComment(content: String, parentCommentId: Long?) {
        viewModelScope.launch {
            _sending.value = true
            val request = SendCommentRequest(commentType, id, parentCommentId, content)
            val success = sendCommentUseCase(request).successOr(false)
            _sending.value = false
            _sendCommentResult.send(if (success) parentCommentId == null else null)
        }
    }

    internal fun deleteComment(commentId: Long) {
        viewModelScope.launch {
            deleteCommentUseCase(
                DeleteCommentRequest(
                    commentType, id, commentId, false
                )
            )
        }
    }
}