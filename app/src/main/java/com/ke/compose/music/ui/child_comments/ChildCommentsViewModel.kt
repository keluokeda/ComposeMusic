package com.ke.compose.music.ui.child_comments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ke.compose.music.db.ChildComment
import com.ke.compose.music.db.ChildCommentDao
import com.ke.compose.music.ui.comments.CommentType
import com.ke.compose.music.ui.comments.LikeCommentRequest
import com.ke.compose.music.ui.comments.LikeCommentUseCase
import com.ke.music.api.HttpService
import com.ke.music.api.response.Comment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ChildCommentsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val childCommentDao: ChildCommentDao,
    httpService: HttpService,
    private val likeCommentUseCase: LikeCommentUseCase
) :
    ViewModel() {
    private val sourceId = savedStateHandle.get<Long>("id")!!
    private val commentType = savedStateHandle.get<CommentType>("type")!!
    private val commentId = savedStateHandle.get<Long>("commentId")!!

    private val _rootComment = MutableStateFlow<Comment?>(null)

    internal val rootComment: StateFlow<Comment?>
        get() = _rootComment

    private val remoteMediator =
        ChildCommentsRemoteMediator(
            httpService,
            commentType,
            sourceId,
            commentId,
            childCommentDao
        ) {
            _rootComment.value = it
        }

    @OptIn(ExperimentalPagingApi::class)
    val comments: Flow<PagingData<ChildComment>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            initialLoadSize = 50
        ),
        remoteMediator = remoteMediator
    ) {
        childCommentDao.getComments()
    }.flow
        .cachedIn(viewModelScope)

    internal fun toggleLiked(comment: ChildComment) {
        viewModelScope.launch {
            val newComment = comment.copy(
                liked = !comment.liked,
                likedCount = if (comment.liked) comment.likedCount - 1 else comment.likedCount + 1
            )
            childCommentDao.updateItem(newComment)

            val request =
                LikeCommentRequest(sourceId, commentType, comment.commentId, !comment.liked)
            likeCommentUseCase(request)
        }
    }

}