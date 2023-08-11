package com.ke.compose.music.ui.artist_detail

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.ImageLoader
import coil.request.ImageRequest
import com.ke.compose.music.ui.component.AlbumView
import com.ke.compose.music.ui.component.MusicView
import com.ke.compose.music.ui.component.MvView
import com.ke.music.room.db.entity.Album
import com.ke.music.room.db.entity.ArtistDescription
import com.ke.music.room.db.entity.Mv
import com.ke.music.room.entity.MusicEntity
import com.ke.music.viewmodel.ArtistDetailViewModel
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ArtistDetailRoute() {
    val viewModel: ArtistDetailViewModel = hiltViewModel()

    val nameAvatar by viewModel.nameAvatar.collectAsStateWithLifecycle()
    val hotSongs by viewModel.artistHotSongs.collectAsStateWithLifecycle()
    val descriptions by viewModel.artistDescriptions.collectAsStateWithLifecycle()
    val albums by viewModel.artistAlbums.collectAsStateWithLifecycle()
    val mvList by viewModel.artistMvs.collectAsStateWithLifecycle()

    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()


    val density = LocalDensity.current


    val height =
        (scrollBehavior.state.heightOffsetLimit - scrollBehavior.state.heightOffset).absoluteValue


    val toolbarHeight
//    by remember {
//        mutableStateOf(0.dp)
//    }
            = with(density) {
        height.toDp()
    } + 64.dp


//    val heightOffset = scrollBehavior.state.heightOffset

//    Logger.d("heightOffset = $heightOffset ,height = $height , toolbar height = ${toolbarHeight} , ${density.density}")

//    val painter =
//        if (nameAvatar.second.isEmpty()) rememberAsyncImagePainter(model = R.drawable.bg) else rememberAsyncImagePainter(
//            model = nameAvatar.second
//        )

    var bitmap by remember {

        mutableStateOf<Bitmap?>(null)
    }
    val context = LocalContext.current



    LaunchedEffect(key1 = nameAvatar.second) {
        if (nameAvatar.second.isNotEmpty()) {
            val loader = ImageLoader(context)
            val result =
                loader.execute(
                    ImageRequest
                        .Builder(context)
                        .data(nameAvatar.second)
                        .build()
                )

            bitmap = (result.drawable as? BitmapDrawable)?.bitmap
        }
    }



    Scaffold(

        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {


            Box(
                modifier = Modifier

            ) {

                if (height > 0 && bitmap != null) {
//                    AsyncImage(
//                        model = nameAvatar.second,
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(toolbarHeight)
//                    )
                    Image(
                        bitmap = bitmap!!.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(toolbarHeight)
                    )
                }




                LargeTopAppBar(
                    title = { Text(text = nameAvatar.first) },
                    navigationIcon = {
                        IconButton(onClick = { dispatcher?.onBackPressed() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(imageVector = Icons.Default.StarBorder, contentDescription = null)
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = Color.Black.copy(alpha = 0.3f),
//                        titleContentColor = Color.White,
//                        actionIconContentColor = Color.White
                    ),
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier
//                        .onSizeChanged {//有延迟
//                            val toolbarHeightDp = with(density) {
//                                it.height.toDp()
//                            }
//                            Logger.d("onSizeChanged $toolbarHeightDp")
//                        }

//                        .background(
//                            Color.Black.copy(alpha = 0.3f)
//                        )
                    //                    .paint(
                    //                        painterResource(id = R.drawable.bg),
                    //                        contentScale = ContentScale.Crop,
                    //                    )
                    //                    .height(toolbarHeight), scrollBehavior = scrollBehavior
                )


            }


        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val pagerState = rememberPagerState()
            val scope = rememberCoroutineScope()

            TabRow(selectedTabIndex = pagerState.currentPage) {
                ArtistTabRowItem.values().forEachIndexed { index, tabRowItem ->
                    Tab(selected = pagerState.currentPage == index, onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }, text = {
                        Text(text = tabRowItem.title)
                    })
                }
            }

            HorizontalPager(
                pageCount = ArtistTabRowItem.values().size,
                state = pagerState,
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
//                    Text(text = TabRowItem.values()[it].title)

                    when (it) {
                        0 -> {
                            ArtistHotSongs(list = hotSongs)
                        }

                        1 -> {
                            ArtistDescription(list = descriptions)
                        }

                        2 -> {
                            ArtistAlbums(list = albums)
                        }

                        3 -> {
                            ArtistMv(list = mvList)
                        }

                        else -> throw IllegalArgumentException("错误的index $it")
                    }
                }
            }
        }

    }
}

@Composable
private fun ArtistHotSongs(list: List<MusicEntity>) {


    LazyColumn {
        items(list, key = {
            it.musicId
        }) {
            MusicView(musicEntity = it)
        }
    }
}

@Composable
private fun ArtistDescription(list: List<ArtistDescription>) {


    LazyColumn(contentPadding = PaddingValues(16.dp)) {
        items(list, key = {
            it.id
        }) {
            Column {
                Text(text = it.title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = it.content)
                Spacer(modifier = Modifier.height(8.dp))

            }
        }
    }
}

@Composable
private fun ArtistAlbums(
    list: List<Album>
) {
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        items(list, key = {
            it.albumId
        }) {
            AlbumView(album = it)
        }
    }
}

@Composable
private fun ArtistMv(list: List<Mv>) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(4.dp)) {
        items(list, key = {
            it.id
        }) {
            MvView(it)
        }
    }
}


private enum class ArtistTabRowItem(val title: String) {
    HotSongs("热门歌曲"),
    Description("歌手简介"),
    Albums("所有专辑"),
    Mv("精品MV")
}