package com.ke.compose.music.ui.playlist_category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.theme.ComposeMusicTheme
import com.ke.music.api.response.PlaylistCategory

@Composable
fun PlaylistCategoryRoute(
    onBackButtonClick: () -> Unit,
    onCategoryClick: (String) -> Unit
) {

    val viewModel: PlaylistCategoryViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    PlaylistCategoryScreen(
        uiState = uiState,
        onBackButtonClick = onBackButtonClick,
        onCategoryClick = onCategoryClick
    ) {
        viewModel.loadContent()
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalMaterialApi::class
)
@Composable
private fun PlaylistCategoryScreen(
    uiState: PlaylistCategoryUiState,
    onBackButtonClick: () -> Unit,
    onCategoryClick: (String) -> Unit,
    reload: () -> Unit
) {
    Scaffold(topBar = {
        AppTopBar(title = { Text(text = "歌单分类") }, navigationIcon = {
            IconButton(onClick = onBackButtonClick) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        })
    }) { paddingValues ->
        when (uiState) {
            is PlaylistCategoryUiState.Detail -> {
                val scrollState = rememberScrollState()
                FlowRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(scrollState)
                        .padding(horizontal = 4.dp)
                ) {
                    uiState.list.forEach {
                        Chip(onClick = {
                            onCategoryClick(it.name)
                        }, modifier = Modifier.padding(horizontal = 4.dp)) {
//                            Icon(imageVector = Icons.Default.Fireplace, contentDescription = null)
                            Text(
                                text = it.name, style =
                                if (it.hot)
                                    MaterialTheme.typography.bodyMedium.copy(
                                        color = Color.Red
                                    ) else MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                }
            }

            PlaylistCategoryUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues), contentAlignment = Alignment.Center
                ) {
                    OutlinedButton(onClick = reload) {
                        Text(text = "出错了，点我重试")
                    }
                }
            }

            PlaylistCategoryUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PlaylistCategoryScreenPreview() {
    ComposeMusicTheme {
        PlaylistCategoryScreen(
            uiState = PlaylistCategoryUiState.Detail(
                listOf(
                    PlaylistCategory(name = "国语", hot = true),
                    PlaylistCategory(name = "综艺", hot = false),
                    PlaylistCategory(name = "流行", hot = true),
                    PlaylistCategory(name = "话语", hot = false),
                    PlaylistCategory(name = "清晨啊", hot = false),
                    PlaylistCategory(name = "国语", hot = false),
                    PlaylistCategory(name = "综艺好玩", hot = false),
                    PlaylistCategory(name = "流行", hot = false),
                    PlaylistCategory(name = "话语", hot = false),
                    PlaylistCategory(name = "清晨", hot = false),
                )
            ),
            onBackButtonClick = { },
            onCategoryClick = {

            }
        ) {

        }
    }
}