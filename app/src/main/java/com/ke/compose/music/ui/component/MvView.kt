package com.ke.compose.music.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ke.music.common.entity.IMv

//
//@Composable
//@OptIn(ExperimentalMaterial3Api::class)
//fun MvView(mv: Mv) {
//    Card(modifier = Modifier.padding(8.dp), onClick = {
//
//    }) {
//        AsyncImage(
//            model = mv.image,
//            contentDescription = null,
//
//            modifier = Modifier
//                .fillMaxWidth()
//                .aspectRatio(16 / 9f),
//            contentScale = ContentScale.Crop
//        )
//        Column(modifier = Modifier.padding(4.dp)) {
//            Text(text = mv.name, maxLines = 1)
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = mv.artistName,
//                maxLines = 1,
//                style = MaterialTheme.typography.bodySmall
//            )
//        }
//
//    }
//}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MvView(mv: IMv) {
    Card(modifier = Modifier.padding(8.dp), onClick = {

    }) {
        AsyncImage(
            model = mv.image,
            contentDescription = null,

            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.padding(4.dp)) {
            Text(text = mv.name, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = mv.artistName,
                maxLines = 1,
                style = MaterialTheme.typography.bodySmall
            )
        }

    }
}