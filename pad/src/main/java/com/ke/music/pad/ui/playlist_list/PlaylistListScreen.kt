package com.ke.music.pad.ui.playlist_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ke.music.room.db.entity.Playlist

@Composable
fun PlaylistListRoute() {
    val viewModel: PlaylistListViewModel = hiltViewModel()
    val list by viewModel.list.collectAsStateWithLifecycle()
    PlaylistListScreen(
        list = list,
        onSelected = {

        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun PlaylistListScreen(
    list: List<Playlist>,
    onSelected: (Playlist) -> Unit,
) {


    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(list, key = {
            it.id
        }) {
            ListItem(icon = {
                AsyncImage(
                    model = it.coverImgUrl,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }, modifier = Modifier.clickable {
                onSelected(it)
            }) {
                Text(text = it.name)
            }
        }


    }
}
