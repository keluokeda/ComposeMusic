package com.ke.compose.music.ui.child_comments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ke.compose.music.niceCount
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.Avatar
import com.ke.compose.music.ui.theme.ComposeMusicTheme
import com.ke.music.common.entity.IChildComment
import com.ke.music.common.entity.IComment
import com.ke.music.room.entity.QueryChildCommentResult
import com.ke.music.viewmodel.ChildCommentsViewModel
import com.orhanobut.logger.Logger

@Composable
fun ChildCommentsRoute(
    onBackClick: () -> Unit,
) {
    val viewModel: ChildCommentsViewModel = hiltViewModel()
    val comments = viewModel.comments.collectAsLazyPagingItems()

    val rootComment by viewModel.rootComment.collectAsStateWithLifecycle()

    Logger.d("child comment size = ${comments.itemCount}")
    ChildCommentsScreen(comments, rootComment, onBackClick) {
        viewModel.toggleLiked(it)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChildCommentsScreen(
    comments: LazyPagingItems<IChildComment>,
    rootComment: IComment?,
    onBackClick: () -> Unit,
    onThumbClick: (IChildComment) -> Unit,
) {
    Scaffold(topBar = {
        AppTopBar(title = { Text(text = "回复") }, navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })
    }) { padding ->

        LazyColumn(modifier = Modifier.padding(padding)) {
            if (rootComment != null) {
                val name = rootComment.user.name
                val avatar = rootComment.user.avatar
                val time = rootComment.timeString
                val content = rootComment.content
                item {
                    Header(avatar, name, time, content)
                }


            }

            items(
                count = comments.itemCount,
                key = comments.itemKey(key = {
                    it.commentId
                }),
                contentType = comments.itemContentType(

                )
            ) { index ->
                val item = comments[index]
                CommentItem(comment = item!!, onThumbClick = onThumbClick)
            }
        }
    }
}

@Composable
private fun Header(
    avatar: String,
    name: String,
    time: String,
    content: String,
) {
    Column {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Row {
                Avatar(url = avatar)
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(text = name)
                    Text(text = time, style = MaterialTheme.typography.bodySmall)
                }

            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = content)
        }

        Divider(Modifier.height(4.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun HeaderPreview() {
    ComposeMusicTheme {
        Header(avatar = "", name = "汉库克", time = "11:22", content = "我最可爱啦")
    }
}


@Composable
private fun CommentItem(
    comment: QueryChildCommentResult,
    modifier: Modifier = Modifier,
    onThumbClick: (QueryChildCommentResult) -> Unit,
) {
    Column(modifier = modifier) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Avatar(url = comment.userAvatar, size = 40)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = comment.username, style = MaterialTheme.typography.bodySmall)
                    if (comment.beRepliedUsername != null) {
                        Icon(
                            imageVector = Icons.Default.ArrowRight,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = comment.beRepliedUsername ?: "",
                            style = MaterialTheme.typography.bodySmall
                        )

                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = comment.content ?: "")
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


            }
        }
        Divider(modifier = Modifier.height(0.2.dp))
    }
}


@Composable
private fun CommentItem(
    comment: IChildComment,
    modifier: Modifier = Modifier,
    onThumbClick: (IChildComment) -> Unit,
) {
    Column(modifier = modifier) {
        Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Avatar(url = comment.user.avatar, size = 40)
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = comment.user.name, style = MaterialTheme.typography.bodySmall)
                    if (comment.beRepliedUsername != null) {
                        Icon(
                            imageVector = Icons.Default.ArrowRight,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = comment.beRepliedUsername ?: "",
                            style = MaterialTheme.typography.bodySmall
                        )

                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = comment.content ?: "")
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


            }
        }
        Divider(modifier = Modifier.height(0.2.dp))
    }
}