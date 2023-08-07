@file:OptIn(ExperimentalTvMaterial3Api::class)

package com.ke.music.tv.ui.share

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.foundation.lazy.list.items
import androidx.tv.material3.Button
import androidx.tv.material3.Checkbox
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import com.ke.music.api.response.User
import com.ke.music.api.response.mockUser
import com.ke.music.commom.observeWithLifecycle
import com.ke.music.repository.toast
import com.ke.music.tv.IMAGE_SIZE
import com.ke.music.tv.ui.components.Avatar
import com.ke.music.tv.ui.theme.ComposeMusicTheme


@Composable
fun ShareRoute(
) {

    val viewModel: ShareViewModel = hiltViewModel()
    val users by viewModel.users.collectAsStateWithLifecycle()
    val sending by viewModel.sending.collectAsStateWithLifecycle()
    val context = LocalContext.current.applicationContext
    val onBackPressedDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    viewModel.sendResult.observeWithLifecycle {
        if (it) {
            context.toast("分享成功")
            onBackPressedDispatcher?.onBackPressed()
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
    ) { list ->
        viewModel.share(list)
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun ShareScreen(
    users: List<User>,
    title: String,
    subTitle: String,
    cover: String,
    sending: Boolean,
    shareClick: (List<User>) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        var checkedUsers by remember {
            mutableStateOf(listOf<User>())
        }

        Row {
            ShareContent(title, subTitle, cover, checkedUsers.isNotEmpty()) {
                shareClick(checkedUsers)
            }


            TvLazyColumn(
                contentPadding = PaddingValues(vertical = 16.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 32.dp)
            ) {
                items(users, key = {
                    it.userId
                }) {

                    val checked = checkedUsers.contains(it)

                    ListItem(
                        selected = false,
                        onClick = {
                            if (checked) {
                                checkedUsers -= it
                            } else {
                                checkedUsers += it
                            }
                        },
                        headlineContent = {
                            Text(text = it.nickname, maxLines = 1)
                        },
                        supportingContent = {
                            Text(text = it.signature ?: "", maxLines = 1)
                        },
                        leadingContent = {
                            Avatar(url = it.avatarUrl)
                        }, trailingContent = {
                            Icon(
                                imageVector = if (checked) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
                                contentDescription = null,
                                tint = if (checked) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        })
                }
            }
        }

        if (sending) {
            Text(
                text = "提交中",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = .3f))
            )
        }
    }


}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview
@Composable
private fun ShareContent(
    title: String = "分享",
    subTitle: String = "来自汉库克的歌单",
    cover: String = "",
    shareButtonEnable: Boolean = false,
    onShareClick: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .width((IMAGE_SIZE + 32).dp)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = cover,
            contentDescription = null,
            modifier = Modifier.size(IMAGE_SIZE.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, maxLines = 1)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = subTitle, maxLines = 1, style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onShareClick,
            enabled = shareButtonEnable,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
//                .padding(horizontal = 32.dp)
            , contentPadding = PaddingValues(horizontal = 32.dp)
        ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null)
            Text(text = "分享")
        }

        Spacer(modifier = Modifier.height(8.dp))

    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
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

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun ShareScreenPreview() {
//    ComposeMusicTheme {
//        ShareScreen(
//            users = listOf(
//                mockUser,
//            ),
//            title = "汉库克的歌单",
//            subTitle = "汉库克美美哒，爱你摸摸哒",
//            cover = "",
//            sending = false,
//            {}
//        ) { _, _ ->
//        }
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun ShareScreenWithSendingPreview() {
//    ComposeMusicTheme {
//        ShareScreen(
//            users = listOf(
//                mockUser,
//            ),
//            title = "汉库克的歌单",
//            subTitle = "汉库克美美哒，爱你摸摸哒",
//            cover = "",
//            sending = true,
//            {}
//        ) { _, _ ->
//        }
//    }
//}
//
//@Composable
//private fun Header(
//    cover: String,
//    title: String,
//    subTitle: String
//) {
//    Row(
//        Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        AsyncImage(
//            model = cover, contentDescription = null,
//            modifier = Modifier
//                .size(100.dp)
//                .background(MaterialTheme.colorScheme.primary)
//        )
//        Column(modifier = Modifier.padding(start = 8.dp)) {
//            Text(text = title, maxLines = 1)
//            Text(
//                text = subTitle,
//                maxLines = 2,
//                style = MaterialTheme.typography.bodySmall,
//                modifier = Modifier.padding(top = 4.dp)
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//private fun HeaderPreview() {
//    ComposeMusicTheme {
//        Header(
//            cover = "",
//            title = "汉库克的歌单",
//            subTitle = "汉皇重色思倾国，御宇多年求不得。杨家有女初长成，养在深闺人未识"
//        )
//    }
//}