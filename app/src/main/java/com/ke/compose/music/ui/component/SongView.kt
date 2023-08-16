package com.ke.compose.music.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ke.music.common.entity.CommentType
import com.ke.music.common.entity.DownloadStatus
import com.ke.music.common.entity.ISongEntity
import com.ke.music.download.LocalDownloadManager
import com.ke.music.player.service.LocalMusicPlayerController
import com.ke.music.player.service.MusicPlayerController
import com.ke.music.repository.entity.ShareType


data class SongViewAction(
    val text: String,
    val action: () -> Unit,
)

@Composable
fun SongView(
    iSongEntity: ISongEntity,
    actions: List<SongViewAction> = emptyList(),
    onClick: (ISongEntity, MusicPlayerController) -> Unit = { entity, controller ->
        controller.playMusic(entity.song.id)
    },
) {

    val size = 40.dp
    Column {
        val controller = LocalMusicPlayerController.current
        Row(
            modifier = Modifier
                .clickable {
                    onClick(iSongEntity, controller)
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            AsyncImage(
                model = iSongEntity.album.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = iSongEntity.song.name, maxLines = 1)
                Text(
                    text = "${iSongEntity.artists.joinToString("/") { it.name }}-${iSongEntity.album.name}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }


//            if (iSongEntity.status != null) {


            if (iSongEntity.status != DownloadStatus.Idle) {

                val imageVector = when (iSongEntity.status) {
                    DownloadStatus.Downloaded -> Icons.Default.DownloadDone
                    else -> Icons.Default.Downloading
                }

                IconButton(onClick = { }, enabled = false) {
                    Icon(imageVector = imageVector, contentDescription = null)
                }
            }
//            }

            if (iSongEntity.song.mv != 0L) {
                IconButton(onClick = { }) {
                    Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null)

                }
            }


            MusicDropDownMenu(false, iSongEntity, actions)
        }
        Divider(modifier = Modifier.height(0.2.dp))
    }
}

@Composable
private fun MusicDropDownMenu(
    initialExpanded: Boolean = false,
    entity: ISongEntity,
    actions: List<SongViewAction> = emptyList(),
) {
    var expanded by remember {
        mutableStateOf(initialExpanded)
    }
    Box {
        IconButton(onClick = {
            expanded = true
        }) {
            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
        }

        val navigationHandler = LocalNavigationHandler.current

        val downloadManager = LocalDownloadManager.current

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(text = {
                Text(text = "下一首播放")
            }, onClick = {
                expanded = false
            })

            DropdownMenuItem(text = {
                Text(text = "收藏到歌单")
            }, onClick = {
                expanded = false
            })

            DropdownMenuItem(text = {
                Text(text = "评论")
            }, onClick = {
                navigationHandler.navigate(
                    NavigationAction.NavigateToComments(
                        CommentType.Music,
                        entity.song.id
                    )
                )
                expanded = false
            })

            if (entity.status == DownloadStatus.Idle || entity.status == DownloadStatus.Error) {

                DropdownMenuItem(text = {
                    Text(text = "下载")
                }, onClick = {
                    expanded = false
                    downloadManager.downloadMusic(entity.song.id)
                })
            }

            DropdownMenuItem(text = {
                Text(text = "分享")
            }, onClick = {
                expanded = false
                navigationHandler.navigate(
                    NavigationAction.NavigateToShare(
                        ShareType.Song,
                        entity.song.id,
                        entity.song.name,
                        entity.album.name,
                        entity.album.image
                    )
                )
            })

            entity.artists.forEach {
                DropdownMenuItem(text = {
                    Text(text = "歌手：${it.name}")
                }, onClick = {
                    expanded = false
                    navigationHandler.navigate(NavigationAction.NavigateToArtistDetail(it.artistId))
                })
            }

            DropdownMenuItem(text = {
                Text(text = "专辑：${entity.album.name}")
            }, onClick = {
                expanded = false
                navigationHandler.navigate(NavigationAction.NavigateToAlbumDetail(entity.album.albumId))
            })

            actions.forEach { action ->
                DropdownMenuItem(text = { Text(text = action.text) }, onClick = action.action)

            }

        }
    }
}
//
//
//@Preview
//@Composable
//private fun MusicDropDownMenuExpandedTrue() {
//    ComposeMusicTheme {
//        MusicDropDownMenu(true, musicEntity)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MusicViewIdlePreview() {
//    ComposeMusicTheme {
//        MusicView(status = Download.STATUS_DOWNLOAD_IDLE)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MusicViewDownloadingPreview() {
//    ComposeMusicTheme {
//        MusicView(status = Download.STATUS_DOWNLOADING)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MusicViewDownloadedPreview() {
//    ComposeMusicTheme {
//        MusicView(status = Download.STATUS_DOWNLOADED, mv = 1L)
//    }
//}
//
//@Composable
//private fun MusicView(status: Int, mv: Long = 0) {
//
//
//    MusicView(musicEntity = musicEntity, rightButton = {})
//}
//
//private val musicEntity = MusicEntity(
//    musicId = 0L,
//    name = "漫步人生路",
//    mv = 0,
//    album = Album(0L, "最爱", ""),
//    artists = listOf(
//        Artist(0, "邓丽君")
//    ),
//    downloadStatus = 0
//)