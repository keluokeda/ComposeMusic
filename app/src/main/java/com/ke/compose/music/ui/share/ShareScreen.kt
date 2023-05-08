package com.ke.compose.music.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.compose.music.observeWithLifecycle
import com.ke.compose.music.toast
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.Avatar
import com.ke.compose.music.ui.theme.ComposeMusicTheme
import com.ke.music.api.response.User
import com.ke.music.api.response.mockUser


@Composable
fun ShareRoute(
    onBackClick: () -> Unit
) {

    val viewModel: ShareViewModel = hiltViewModel()
    val users by viewModel.users.collectAsStateWithLifecycle()
    val sending by viewModel.sending.collectAsStateWithLifecycle()
    val context = LocalContext.current.applicationContext
    viewModel.sendResult.observeWithLifecycle {
        if (it) {
            context.toast("分享成功")
            onBackClick()
        } else {
            context.toast("分享失败")
        }
    }
    ShareScreen(
        users = users,
        title = viewModel.title,
        subTitle = viewModel.subTitle,
        cover = viewModel.cover,
        sending = sending,
        onBackClick
    ) { list, content ->
        viewModel.share(list, content)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ShareScreen(
    users: List<User>,
    title: String,
    subTitle: String,
    cover: String,
    sending: Boolean,
    onBackClick: () -> Unit,
    shareClick: (List<User>, content: String) -> Unit
) {


    var content by remember {
        mutableStateOf("")
    }

    var checkedUsers by remember {
        mutableStateOf(listOf<User>())
    }

    Scaffold(topBar = {
        AppTopBar(
            title = { Text(text = "分享") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }, actions = {
                IconButton(onClick = {
                    shareClick(checkedUsers, content)
                }, enabled = checkedUsers.isNotEmpty() && !sending) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = null)
                }
            })
    }) { padding ->

        if (sending) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding), contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {

            Column(Modifier.padding(padding)) {
                Header(cover = cover, title = title, subTitle = subTitle)

                Text(
                    text = "分享给：",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )

                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(users, key = {
                        it.userId
                    }) {
                        CheckableUser(
                            user = it,
                            checked = checkedUsers.contains(it),
                            onCheckedChange = { _ ->
                                val newUserList = checkedUsers.toMutableList()
                                if (checkedUsers.contains(it)) {
                                    newUserList.remove(it)
                                } else {
                                    newUserList.add(it)
                                }
                                checkedUsers = newUserList
                            }
                        )
                    }
                }

                TextField(
                    value = content, onValueChange = {
                        content = it
                    }, modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = "内容")
                    }
                )
            }
        }
    }
}

@Composable
fun CheckableUser(user: User, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(url = user.avatarUrl, size = 40)
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
        ) {
            Text(text = user.nickname)
            if (!user.signature.isNullOrEmpty()) {
                Text(
                    text = user.signature ?: "",
                    maxLines = 1,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CheckableUserPreview() {
    ComposeMusicTheme {
        val user = mockUser
        CheckableUser(user = user, checked = true, onCheckedChange = {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShareScreenPreview() {
    ComposeMusicTheme {
        ShareScreen(
            users = listOf(
                mockUser,
            ),
            title = "汉库克的歌单",
            subTitle = "汉库克美美哒，爱你摸摸哒",
            cover = "",
            sending = false,
            {}
        ) { _, _ ->
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShareScreenWithSendingPreview() {
    ComposeMusicTheme {
        ShareScreen(
            users = listOf(
                mockUser,
            ),
            title = "汉库克的歌单",
            subTitle = "汉库克美美哒，爱你摸摸哒",
            cover = "",
            sending = true,
            {}
        ) { _, _ ->
        }
    }
}

@Composable
private fun Header(
    cover: String,
    title: String,
    subTitle: String
) {
    Row(
        Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = cover, contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
        Column(modifier = Modifier.padding(start = 8.dp)) {
            Text(text = title, maxLines = 1)
            Text(
                text = subTitle,
                maxLines = 2,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HeaderPreview() {
    ComposeMusicTheme {
        Header(
            cover = "",
            title = "汉库克的歌单",
            subTitle = "汉皇重色思倾国，御宇多年求不得。杨家有女初长成，养在深闺人未识"
        )
    }
}