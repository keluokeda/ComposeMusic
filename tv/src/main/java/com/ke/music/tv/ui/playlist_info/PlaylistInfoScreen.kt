package com.ke.music.tv.ui.playlist_info


import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.TvLazyColumn
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage


@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun PlaylistInfoScreen(
    name: String,
    description: String,
    image: String,
) {


    Box(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = image,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )


        TvLazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .focusable()
        ) {
            item {

                Text(
                    text = name,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = description, modifier = Modifier
                        .padding(16.dp)

                )


            }


        }
    }


}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun PlaylistInfoScreenPreview() {

    PlaylistInfoScreen(
        name = "[听·邓丽君] 你的歌声这样熟悉",
        description = "“有华人的地方，就有邓丽君。\"\n邓丽君曾引领整个东南亚的音乐审美，是华语歌坛不朽的传奇，更是时代文化的缩影。耀眼的三一多年音乐生涯间，邓丽君用多种语言演唱了上千首歌，聚焦着千万歌迷的目光。她演绎不同风格的超前思想为华语流行乐的发展留下深远影响。邓丽君的歌融合东西方元素，民族风格碰撞流行，穿旗袍唱中文歌，向世界展示了中国女性的自信之美。",
        image = "https://p1.music.126.net/K8kwFu5OC4YD2jsD6ik8qQ==/109951168308773359.jpg"
    )
}