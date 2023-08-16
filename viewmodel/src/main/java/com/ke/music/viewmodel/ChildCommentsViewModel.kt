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
import com.ke.music.common.domain.LikeCommentUseCase
import com.ke.music.common.entity.CommentType
import com.ke.music.common.entity.IChildComment
import com.ke.music.common.entity.LikeCommentRequest
import com.ke.music.common.mediator.ChildCommentsRemoteMediator
import com.ke.music.common.repository.CommentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChildCommentsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val likeCommentUseCase: LikeCommentUseCase,
    private val commentRepository: CommentRepository,
    remoteMediator: ChildCommentsRemoteMediator,
) :
    ViewModel() {
    private val sourceId = savedStateHandle.get<Long>("id")!!
    private val commentType = savedStateHandle.get<CommentType>("type")!!
    private val commentId = savedStateHandle.get<Long>("commentId")!!


    val rootComment = commentRepository.findComment(commentId).stateIn(
        viewModelScope, SharingStarted.Eagerly, null
    )


    init {
        remoteMediator.sourceId = sourceId
        remoteMediator.commentType = commentType
        remoteMediator.commentId = commentId
    }


    @OptIn(ExperimentalPagingApi::class)
    val comments: Flow<PagingData<IChildComment>> = Pager(
        config = PagingConfig(
            pageSize = 50,
            enablePlaceholders = false,
            initialLoadSize = 50
        ),
        remoteMediator = remoteMediator
    ) {
        commentRepository.getChildComments(commentId) as PagingSource<Int, IChildComment>
    }.flow
        .cachedIn(viewModelScope)

    fun toggleLiked(childComment: IChildComment) {
        viewModelScope.launch {
            likeCommentUseCase(
                LikeCommentRequest(
                    sourceId,
                    commentType,
                    childComment.commentId,
                    true
                )
            )
        }
    }

}