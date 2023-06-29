package com.ke.compose.music.ui.child_comments

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ke.compose.music.entity.QueryChildCommentResult
import com.ke.compose.music.repository.ChildCommentRepository
import com.ke.compose.music.repository.CommentRepository
import com.ke.compose.music.ui.comments.CommentType
import com.ke.compose.music.ui.comments.LikeCommentRequest
import com.ke.compose.music.ui.comments.LikeCommentUseCase
import com.ke.music.api.HttpService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ChildCommentsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    httpService: HttpService,
    private val likeCommentUseCase: LikeCommentUseCase,
    private val childCommentRepository: ChildCommentRepository,
    private val commentRepository: CommentRepository
) :
    ViewModel() {
    private val sourceId = savedStateHandle.get<Long>("id")!!
    private val commentType = savedStateHandle.get<CommentType>("type")!!
    private val commentId = savedStateHandle.get<Long>("commentId")!!


    internal val rootComment = commentRepository.queryCommentAndUser(commentId).stateIn(
        viewModelScope, SharingStarted.Eagerly, null
    )


    private val remoteMediator =
        ChildCommentsRemoteMediator(
            httpService,
            commentType,
            sourceId,
            commentId,
            childCommentRepository
        )

    @OptIn(ExperimentalPagingApi::class)
    val comments: Flow<PagingData<QueryChildCommentResult>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            initialLoadSize = 50
        ),
        remoteMediator = remoteMediator
    ) {
        childCommentRepository.getChildComments(commentId)
    }.flow
        .cachedIn(viewModelScope)

    internal fun toggleLiked(childComment: QueryChildCommentResult) {
        viewModelScope.launch {
            likeCommentUseCase(
                LikeCommentRequest(
                    sourceId,
                    commentType,
                    childComment.commentId,
                    !childComment.liked,
                    true
                )
            )
        }
    }

}