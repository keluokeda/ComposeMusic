package com.ke.compose.music.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ke.compose.music.db.entity.Album
import com.ke.compose.music.db.entity.Artist
import com.ke.compose.music.db.entity.Download
import com.ke.compose.music.entity.MusicEntity
import com.ke.compose.music.ui.IAppViewModel
import com.ke.compose.music.ui.LocalAppViewModel
import com.ke.compose.music.ui.theme.ComposeMusicTheme


@Composable
fun MusicView(
    musicEntity: MusicEntity,
    rightButton: @Composable () -> Unit = {},
    onClick: (MusicEntity, IAppViewModel) -> Unit = { entity, viewModel ->
        viewModel.playMusic(entity.musicId)
    }
) {
    val size = 40.dp
    Column {
        val appViewModel = LocalAppViewModel.current
        Row(
            modifier = Modifier
                .clickable {
                    onClick(musicEntity, appViewModel)
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            AsyncImage(
                model = musicEntity.album.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(size)
                    .background(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = musicEntity.name, maxLines = 1)
                Text(
                    text = "${musicEntity.artists.joinToString("/") { it.name }}-${musicEntity.album.name}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1
                )
            }


            if (musicEntity.downloadStatus != null) {

                val imageVector = when (musicEntity.downloadStatus) {
                    Download.STATUS_DOWNLOADED -> Icons.Default.DownloadDone
                    else -> Icons.Default.Downloading
                }

                IconButton(onClick = { }, enabled = false) {
                    Icon(imageVector = imageVector, contentDescription = null)
                }
            }

            if (musicEntity.mv != 0L) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null)

                }
            }

            rightButton()
        }
        Divider(modifier = Modifier.height(0.2.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun MusicViewIdlePreview() {
    ComposeMusicTheme {
        MusicView(status = Download.STATUS_DOWNLOAD_IDLE)
    }
}

@Preview(showBackground = true)
@Composable
fun MusicViewDownloadingPreview() {
    ComposeMusicTheme {
        MusicView(status = Download.STATUS_DOWNLOADING)
    }
}

@Preview(showBackground = true)
@Composable
fun MusicViewDownloadedPreview() {
    ComposeMusicTheme {
        MusicView(status = Download.STATUS_DOWNLOADED, mv = 1L)
    }
}

@Composable
private fun MusicView(status: Int, mv: Long = 0) {
    val musicEntity = MusicEntity(
        musicId = 0L,
        name = "漫步人生路",
        mv = mv,
        album = Album(0L, "最爱", ""),
        artists = listOf(
            Artist(0, "邓丽君")
        ),
        downloadStatus = status
    )


    MusicView(musicEntity = musicEntity, rightButton = {})
}