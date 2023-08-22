package com.ke.compose.music.ui.all_mv

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.ke.compose.music.ui.component.AppTopBar
import com.ke.compose.music.ui.component.MvView
import com.ke.music.common.entity.IMv
import com.ke.music.viewmodel.AllMvViewModel
import com.orhanobut.logger.Logger


private val areaList = listOf("全部", "内地", "港台", "欧美", "日本", "韩国")

private val typeList = listOf("全部", "官方版", "原生", "现场版", "网易出品")

@Composable
fun AllMvRoute() {
    val viewModel: AllMvViewModel = hiltViewModel()

    val list = viewModel.allMv.collectAsLazyPagingItems()
    Logger.d("list state = ${list.loadState}")

    val area by viewModel.area.collectAsStateWithLifecycle()
    val type by viewModel.type.collectAsStateWithLifecycle()

    AllMvScreen(list = list, area, type, {
        viewModel.updateArea(it)
        list.refresh()
    }, {
        viewModel.updateType(it)
        list.refresh()
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllMvScreen(
    list: LazyPagingItems<IMv>,
    area: String,
    type: String,
    onAreaUpdate: (String) -> Unit,
    onTypeUpdate: (String) -> Unit,
) {

    val dispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher

    Scaffold(topBar = {
        AppTopBar(
            title = { Text(text = "全部MV") },
            navigationIcon = {
                IconButton(onClick = {
                    dispatcher?.onBackPressed()
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }, actions = {
                Box {
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    TextButton(onClick = {
                        expanded = true
                    }) {
                        Text(text = area)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        areaList.forEach { item ->
                            DropdownMenuItem(text = {
                                Text(text = item)
                            }, onClick = {
                                onAreaUpdate(item)
                                expanded = false
                            })
                        }
                    }
                }

                Box {
                    var expanded by remember {
                        mutableStateOf(false)
                    }
                    TextButton(onClick = {
                        expanded = true
                    }) {
                        Text(text = type)
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        typeList.forEach { item ->
                            DropdownMenuItem(text = {
                                Text(text = item)
                            }, onClick = {
                                onTypeUpdate(item)
                                expanded = false
                            })
                        }
                    }
                }

            })
    }) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(list.itemCount, key = list.itemKey {
                it.id
            }, contentType = list.itemContentType()) {
                MvView(mv = list[it]!!)
            }
        }
    }
}

