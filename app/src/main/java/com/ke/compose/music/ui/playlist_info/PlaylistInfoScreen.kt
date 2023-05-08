package com.ke.compose.music.ui.playlist_info


import android.graphics.Bitmap
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmapOrNull
import coil.compose.AsyncImage
import com.ke.compose.music.saveBitmapToGallery
import com.ke.compose.music.toast
import com.ke.compose.music.ui.component.AppTopBar
import com.orhanobut.logger.Logger
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistInfoScreen(
    name: String,
    description: String,
    image: String,
    onBackButtonClick: () -> Unit
) {

    var bitmap: Bitmap? by remember {
        mutableStateOf(null)
    }

    Scaffold(topBar = {
        AppTopBar(title = { Text(text = name, maxLines = 1) }, navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })
    }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            item {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.FillWidth,
                    onSuccess = { state ->
                        bitmap = state.result.drawable.toBitmapOrNull()
                        Logger.d("bitmap = $bitmap")
                    }
                )
                Text(text = description, modifier = Modifier.padding(16.dp))


            }



            item {
                val scope = rememberCoroutineScope()
                val context = LocalContext.current.applicationContext
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                ) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                if (bitmap != null) {
                                    context.saveBitmapToGallery(bitmap!!)
                                    context.toast("保存成功")
                                } else {
                                    Logger.d("bitmap = null")
                                }
                            }
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "保存封面到相册")
                    }
                }
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
    ) {

    }
}