package com.ke.compose.music.ui.users

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.ke.compose.music.db.User
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.Avatar
import com.ke.compose.music.ui.component.LocalBackHandler
import com.ke.compose.music.ui.theme.ComposeMusicTheme

@Composable
fun UsersRoute() {
    val viewModel: UsersViewModel = hiltViewModel()
    val users = viewModel.users.collectAsLazyPagingItems()
    UserScreen(users, viewModel.title) {
        viewModel.toggleFollow(it)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserScreen(users: LazyPagingItems<User>, title: String, toggleFollow: (User) -> Unit) {
    val backHandler = LocalBackHandler.current
    Scaffold(topBar = {
        AppTopBar(title = {
            Text(text = title)
        }, navigationIcon = {
            IconButton(onClick = {
                backHandler.navigateBack()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })
    }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(users, key = { it.userId }) {
                UserItem(user = it!!) {
                    toggleFollow(it)
                }
            }
        }
    }
}

@Composable
private fun UserItem(user: User, followClick: () -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Avatar(url = user.avatar, size = 40)
        Column(
            Modifier
                .weight(1f)
                .padding(horizontal = 4.dp)
        ) {
            Text(text = user.name)
            if (user.signature.isNotEmpty()) {
                Text(
                    text = user.signature,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }
        }
        TextButton(onClick = followClick) {
            Text(text = if (user.followed) "已关注" else "关注")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemNormalPreview() {
    ComposeMusicTheme {
        UserItem(user = createUserWithSignatureAndFollowed("", false)) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserItemFollowedPreview() {
    ComposeMusicTheme {
        UserItem(user = createUserWithSignatureAndFollowed("爱路飞一辈子", true)) {

        }
    }
}


private fun createUserWithSignatureAndFollowed(signature: String, followed: Boolean): User {
    return User(
        id = 0,
        userId = 0,
        name = "汉库克",
        avatar = "",
        signature = signature,
        followed = followed,
        usersType = UsersType.PlaylistSubscribers,
        sourceId = 0
    )
}