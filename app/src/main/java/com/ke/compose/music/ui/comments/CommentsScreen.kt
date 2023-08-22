package com.ke.compose.music.ui.comments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ke.compose.music.keyboardAsState
import com.ke.compose.music.niceCount
import com.ke.compose.music.observeWithLifecycle
import com.ke.compose.music.toast
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.Avatar
import com.ke.music.common.entity.CommentType
import com.ke.music.common.entity.IComment
import com.ke.music.viewmodel.CommentsViewModel


@Composable
fun CommentsRoute(
    onBackButtonClick: () -> Unit,
    onMoreCommentClick: (Long, CommentType, Long) -> Unit,
) {
    val viewModel: CommentsViewModel = hiltViewModel()

    val sending by viewModel.sending.collectAsStateWithLifecycle()
    val list = viewModel.comments.collectAsLazyPagingItems()
    val context = LocalContext.current.applicationContext
    viewModel.sendCommentResult.observeWithLifecycle {
        if (it != null) {
            if (it) {
                list.refresh()
            }
            context.toast("评论成功")
        } else {
            context.toast("评论失败")
        }
    }

    CommentScreen(list, sending, onBackButtonClick, {
        viewModel.toggleLiked(it)
    }, { content, selected ->
        viewModel.sendComment(content, selected?.commentId)
    }, canDeleteComment = {
        viewModel.canDeleteComment(it)
    }, deleteComment = {
        viewModel.deleteComment(it)
    }, {
        onMoreCommentClick(viewModel.id, viewModel.commentType, it)
    })
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
)
@Composable
private fun CommentScreen(
    list: LazyPagingItems<IComment>,
    sending: Boolean,
    onBackButtonClick: () -> Unit,
    onThumbClick: (IComment) -> Unit,
    onSendComment: (String, IComment?) -> Unit,
    canDeleteComment: (IComment) -> Boolean,
    deleteComment: (Long) -> Unit,
    onMoreCommentClick: (Long) -> Unit,
) {
    var text by remember {
        mutableStateOf("")
    }

    Scaffold(topBar = {
        AppTopBar(
            title = {
                Text(text = "评论")
            }, navigationIcon = {
                IconButton(onClick = onBackButtonClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            })
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            val keyboardController = LocalSoftwareKeyboardController.current

            val focusRequester = remember {
                FocusRequester()
            }


            //要删除的评论
            var deleteTargetComment by remember {
                mutableStateOf<IComment?>(null)
            }


            var selectedComment by remember {
                mutableStateOf<IComment?>(null)
            }


            val isKeyboardOpen by keyboardAsState()

            if (!isKeyboardOpen) {
                selectedComment = null
            }

            var isDialogOpen by remember {
                mutableStateOf(false)
            }


            if (isDialogOpen) {
                AlertDialog(
                    onDismissRequest = {
                        isDialogOpen = false
                        deleteTargetComment = null
                    },
                    title = {
                        Text(text = "提示")
                    },
                    text = {
                        Text(text = "是否删除该评论")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            isDialogOpen = false
                            deleteComment(deleteTargetComment?.commentId ?: 0L)
                        }) {
                            Text(text = "删除")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { isDialogOpen = false }) {
                            Text(text = "取消")
                        }
                    }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {


                items(
                    count = list.itemCount,
                    key = list.itemKey(key = {
                        it.commentId
                    }),
                    contentType = list.itemContentType(
                    )
                ) { index ->
                    val item = list[index]
                    CommentItem(
                        comment = item!!,

                        onThumbClick = {
                            onThumbClick(it)
                        },
                        onMoreCommentClick = {
                            onMoreCommentClick(item.commentId)
                        },
                        onClick = {

                            focusRequester.requestFocus()
                            keyboardController?.show()
                            selectedComment = it
                        }, onLongClick = { comment ->
//                                comment ->
                            if (canDeleteComment(comment)) {
                                isDialogOpen = true
                                deleteTargetComment = comment
                            }
                        })
                }

            }



            TextField(
                enabled = !sending,
                value = text,
                onValueChange = {
                    text = it
                },
                label = {
                    Text(text = "评论")
                },
                placeholder = {
                    Text(text = if (selectedComment == null) "" else "回复${selectedComment?.user?.name}")
                },

                trailingIcon = {
                    IconButton(onClick = {
                        onSendComment(text, selectedComment)
                        text = ""
                    }, enabled = !sending && text.isNotEmpty()) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                shape = OutlinedTextFieldDefaults.shape
            )
        }

    }
}
//
//
//@Composable
//private fun CommentItem(
//    comment: QueryCommentResult,
//    modifier: Modifier = Modifier,
//    onThumbClick: () -> Unit,
//    onMoreCommentClick: () -> Unit,
//    onClick: () -> Unit = {},
//    onLongClick: () -> Unit = {},
//) {
//
//
//    Column(modifier = modifier
//        .pointerInput(Unit) {
//            detectTapGestures(onLongPress = {
////                Logger.d("长按了 $comment")
//                onLongClick()
//            }, onTap = {
//                onClick()
//            })
//        }) {
//        Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
//            Avatar(url = comment.userAvatar, size = 40)
//            Spacer(modifier = Modifier.width(8.dp))
//            Column {
//                Text(text = comment.username, style = MaterialTheme.typography.bodySmall)
//                Spacer(modifier = Modifier.height(2.dp))
//                Text(text = comment.content)
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Text(
//                        text = "${comment.timeString} ${comment.ip}",
//                        style = MaterialTheme.typography.bodySmall
//                    )
//                    Spacer(modifier = Modifier.weight(1f))
//                    IconButton(onClick = {
//                        onThumbClick()
//                    }) {
//                        Row(verticalAlignment = Alignment.CenterVertically) {
//                            if (comment.likedCount > 0)
//                                Text(
//                                    text = comment.likedCount.niceCount(),
//                                    style = MaterialTheme.typography.bodySmall
//                                )
//                            Icon(
//                                imageVector = if (comment.liked) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
//                                contentDescription = null,
//                                tint = Color.Gray,
//                                modifier = Modifier.size(18.dp)
//                            )
//                        }
//
//
//                    }
//                }
//
//                if (comment.replyCount > 0)
//
//                    Text(
//                        text = comment.replyCount.niceCount() + "条回复>",
//                        style = MaterialTheme.typography.bodySmall,
//                        modifier = Modifier.clickable {
//                            onMoreCommentClick()
//                        }
//                    )
//
//            }
//        }
//        Divider(modifier = Modifier.height(0.2.dp))
//    }
//
//
//}


@Composable
private fun CommentItem(
    comment: IComment,
    modifier: Modifier = Modifier,
    onThumbClick: (IComment) -> Unit,
    onMoreCommentClick: () -> Unit,
    onClick: (IComment) -> Unit = {},
    onLongClick: (IComment) -> Unit = {},
) {


    Column(modifier = modifier
        .pointerInput(Unit) {
            detectTapGestures(onLongPress = {
//                Logger.d("长按了 $comment")
                onLongClick(comment)
            }, onTap = {
                onClick(comment)
            })
        }) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Avatar(url = comment.user.avatar, size = 40)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = comment.user.name, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = comment.content)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${comment.timeString} ${comment.ip}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        onThumbClick(comment)
                    }) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (comment.likedCount > 0)
                                Text(
                                    text = comment.likedCount.niceCount(),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            Icon(
                                imageVector = if (comment.liked) Icons.Default.ThumbUp else Icons.Outlined.ThumbUp,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                        }


                    }
                }

                if (comment.replyCount > 0)

                    Text(
                        text = comment.replyCount.niceCount() + "条回复>",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.clickable {
                            onMoreCommentClick()
                        }
                    )

            }
        }
        Divider(modifier = Modifier.height(0.2.dp))
    }


}

//
//@Preview(showBackground = true)
//@Composable
//fun CommentItemNoChildCommentPreview() {
//    ComposeMusicTheme {
//        CommentItem(
//            comment = Comment(
//                0,
//                0,
//                "汉库克",
//                "",
//                0,
//                "好喜欢路飞啊",
//                "1分钟前",
//                0,
//                10,
//                "北京",
//                owner = false,
//                liked = true,
//                replyCount = 0
//            ),
//            onThumbClick = {},
//            onMoreCommentClick = {}
//        )
//    }
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun CommentItemPreview() {
//    ComposeMusicTheme {
//        CommentItem(
//            comment = Comment(
//                0,
//                0,
//                "汉库克",
//                "",
//                0,
//                "好喜欢路飞啊",
//                "1分钟前",
//                0,
//                10,
//                "北京",
//                owner = false,
//                liked = true,
//                replyCount = 10
//            ),
//            onThumbClick = {},
//            onMoreCommentClick = {}
//        )
//    }
//}

