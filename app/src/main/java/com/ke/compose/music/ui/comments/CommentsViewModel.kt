package com.ke.compose.music.ui.comments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ke.compose.music.db.Comment
import com.ke.compose.music.db.CommentDao
import com.ke.compose.music.domain.successOr
import com.ke.music.api.HttpService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
internal class CommentsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    httpService: HttpService,
    private val commentDao: CommentDao,
    private val likeCommentUseCase: LikeCommentUseCase,
    private val sendCommentUseCase: SendCommentUseCase
) : ViewModel() {
    internal val id = savedStateHandle.get<Long>("id")!!

    internal val commentType: CommentType = savedStateHandle.get<CommentType>("type")!!

    private val _sortType = MutableStateFlow(3)

    internal val sortType: StateFlow<Int>
        get() = _sortType

    private val _sending = MutableStateFlow(false)
    internal val sending: StateFlow<Boolean>
        get() = _sending

    private val _sendCommentResult = Channel<Boolean>(capacity = Channel.CONFLATED)

    /**
     * 发送评论结果
     */
    internal val sendCommentResult: Flow<Boolean>
        get() = _sendCommentResult.receiveAsFlow()

    private val remoteMediator = CommentsRemoteMediator(
        commentDao,
        httpService,
        id,
        commentType,
        _sortType.value
    )

    val comments: Flow<PagingData<Comment>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            initialLoadSize = 50
        ),
        remoteMediator = remoteMediator
    ) {
        commentDao.getComments()
    }.flow
        .cachedIn(viewModelScope)

    internal fun toggleLiked(comment: Comment) {
        viewModelScope.launch {
            val newComment = comment.copy(
                liked = !comment.liked,
                likedCount = if (comment.liked) comment.likedCount - 1 else comment.likedCount + 1
            )
            commentDao.updateItem(newComment)

            val request = LikeCommentRequest(id, commentType, comment.commentId, !comment.liked)
            likeCommentUseCase(request)
        }
    }

    internal fun toggleSortType(type: Int) {
        _sortType.value = type
        remoteMediator.sortType = type
    }

    /**
     * 发布评论
     */
    internal fun sendComment(content: String) {
        viewModelScope.launch {
            _sending.value = true
            val request = SendCommentRequest(commentType, id, null, content)
            val success = sendCommentUseCase(request).successOr(false)
            _sending.value = false
            _sendCommentResult.send(success)
        }
    }
}