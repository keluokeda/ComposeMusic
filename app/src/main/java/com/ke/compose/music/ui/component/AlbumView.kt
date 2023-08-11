package com.ke.compose.music.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ke.music.room.db.entity.Album

@Composable
fun AlbumView(
    album: Album
) {
    val navigationHandler = LocalNavigationHandler.current
    Box(modifier = Modifier
        .fillMaxSize()
        .aspectRatio(1f)
        .clickable {
            navigationHandler.navigate(NavigationAction.NavigateToAlbumDetail(album.albumId))
        }
        .padding(2.dp), contentAlignment = Alignment.BottomEnd) {
        AsyncImage(
            model = album.imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = .6f))
                .padding(4.dp)
        ) {
            Text(text = album.name, maxLines = 1)

        }
    }
}