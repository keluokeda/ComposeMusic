package com.ke.compose.music.ui.message

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.compose.music.niceTime
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.Avatar
import com.ke.compose.music.ui.main.MainScreenFragment
import com.ke.compose.music.ui.theme.ComposeMusicTheme
import com.ke.music.api.response.PrivateMessage
import com.ke.music.api.response.mockPrivateMessage
import org.json.JSONObject

@Composable
fun MessageRoute() {

    val viewModel: MessageViewModel = hiltViewModel()

    val list
            by viewModel.list.collectAsStateWithLifecycle()

    val refreshing by viewModel.refreshing.collectAsStateWithLifecycle()
    MessageScreen(messageList = list, refreshing = refreshing) {
        viewModel.refresh()
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MessageScreen(messageList: List<PrivateMessage>, refreshing: Boolean, onRefresh: () -> Unit) {


    val state = rememberPullRefreshState(refreshing, onRefresh)

    Scaffold(topBar = {
        AppTopBar(
            title = {
                Text(text = MainScreenFragment.Message.label)
            }
        )
    }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .pullRefresh(state)
        ) {
            LazyColumn {
                items(messageList, key = { message -> message.time }) { message ->
                    MessageItem(message = message, onClick = {})
                }
            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = state,
                Modifier.align(Alignment.TopCenter)
            )
        }
    }

}

@Composable
fun MessageItem(
    message: PrivateMessage,
    onClick: (PrivateMessage) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(modifier = modifier
            .clickable {
                onClick(message)
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Avatar(url = message.fromUser.avatarUrl)
            Spacer(modifier = Modifier.size(width = 8.dp, height = 0.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = message.fromUser.nickname, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = message.message() ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.size(width = 8.dp, height = 0.dp))
            Text(text = message.time.niceTime(), style = MaterialTheme.typography.bodySmall)
        }

        Divider(startIndent = 16.dp)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun MessageScreenPreview() {


    ComposeMusicTheme {
        MessageScreen(messageList = listOf(mockPrivateMessage), refreshing = false) {
        }
    }
}

fun PrivateMessage.message(): String? {
    return JSONObject(lastMessage).optString("msg")
}