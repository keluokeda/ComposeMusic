package com.ke.music.tv.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import coil.compose.AsyncImage

/**
 * 头像
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun Avatar(modifier: Modifier = Modifier, url: String, size: Int = 48) {
    AsyncImage(
        model = url, contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    )

}

//
//
//@Composable
//fun SongView(
//    song: Song,
//    modifier: Modifier = Modifier,
//    onMoreButtonClick: (Song) -> Unit
//) {
//    val size = 40.dp
//    val musicController = LocalMusicController.current
//    Column {
//        Row(modifier = modifier
//            .clickable {
//                musicController.sendAction(MusicControllerAction.PlayNow(song.id))
//            }
//            .padding(horizontal = 16.dp, vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically) {
//
//
//            AsyncImage(
//                model = song.album.imageUrl,
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(size)
//                    .background(MaterialTheme.colorScheme.primary)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Column(modifier = Modifier.weight(1f)) {
//                Text(text = song.name, maxLines = 1)
//                Text(
//                    text = "${song.singers.joinToString("/") { it.name }}-${song.album.name}",
//                    style = MaterialTheme.typography.bodySmall,
//                    maxLines = 1
//                )
//            }
//            if (song.mv != 0L) {
//                IconButton(onClick = { /*TODO*/ }) {
//                    Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null)
//
//                }
//            }
//
//
//
//
//
//
//
//            IconButton(onClick = {
//                onMoreButtonClick(song)
//            }) {
//                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
//            }
//        }
//
//
//
//        Divider(modifier = Modifier.height(0.2.dp))
//    }
//}
//
//@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun SongViewHasMvPreview() {
//    val song = Song(
//        10, "我只在乎你", Album(0, "温情如水国语集", ""), listOf(
//            Singer(0, "邓丽君")
//        ), 100
//    )
//    ComposeMusicTheme {
//        SongView(song = song) {
//
//        }
//    }
//}
//
//
//@Composable
//fun PlaylistView(name: String, cover: String, onClick: () -> Unit) {
//    Box(modifier = Modifier
//        .clickable {
//            onClick()
//        }
//        .aspectRatio(1f), contentAlignment = Alignment.BottomCenter) {
//        AsyncImage(
//            model = cover,
//            contentDescription = null,
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.fillMaxSize()
//        )
//        Text(
//            text = "$name\n",
//            style = MaterialTheme.typography.bodySmall,
//            color = Color.White,
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    Color.Black.copy(
//                        alpha = 0.3f
//                    )
//                )
//        )
//    }
//}
//
//
//@Preview(widthDp = 120, heightDp = 200, showBackground = true)
//@Composable
//fun PlaylistViewPreview() {
//    ComposeMusicTheme {
//        Column {
//            PlaylistView(name = "汉库克美美哒", cover = "") {
//
//            }
//        }
//    }
//}
//
//fun <T : Any> LazyGridScope.items(
//    items: LazyPagingItems<T>,
//    key: ((item: T) -> Any)? = null,
//    span: ((item: T) -> GridItemSpan)? = null,
//    contentType: ((item: T) -> Any)? = null,
//    itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit
//) {
//    items(
//        count = items.itemCount,
//        key = if (key == null) null else { index ->
//            val item = items.peek(index)
//            if (item == null) {
////                PagingPlaceholderKey(index)
//            } else {
//                key(item)
//            }
//        },
//        span = if (span == null) null else { index ->
//            val item = items.peek(index)
//            if (item == null) {
//                GridItemSpan(1)
//            } else {
//                span(item)
//            }
//        },
//        contentType = if (contentType == null) {
//            { null }
//        } else { index ->
//            val item = items.peek(index)
//            if (item == null) {
//                null
//            } else {
//                contentType(item)
//            }
//        }
//    ) { index ->
//        itemContent(items[index])
//    }
//}