package com.ke.compose.music.ui.component

//
//
//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun SongBottomSheetLayout(
//    selectedSong: Song?,
//    sheetState: ModalBottomSheetState,
//    actions: @Composable () -> Unit,
//    content: @Composable () -> Unit
//) {
//
//    val scope = rememberCoroutineScope()
//    val navigationHandler = LocalNavigationHandler.current
//
//    ModalBottomSheetLayout(sheetContent = {
//        Column {
//            ListItem(icon = {
//                Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null)
//            }) {
//                Text(text = "下一首播放")
//            }
//
//            val appViewModel = LocalAppViewModel.current
//
//            ListItem(icon = {
//                Icon(imageVector = Icons.Default.LibraryAdd, contentDescription = null)
//            }, modifier = Modifier.clickable {
//                scope.launch {
//                    sheetState.hide()
//                    appViewModel.selectedSongList = listOf(selectedSong!!.id)
//                    navigationHandler.navigate(
//                        NavigationAction.NavigateToPlaylistList
//                    )
//                }
//
//            }) {
//                Text(text = "收藏到歌单")
//            }
//
//
//            ListItem(icon = {
//                Icon(imageVector = Icons.Default.Comment, contentDescription = null)
//            }, modifier = Modifier.clickable {
//
//                scope.launch {
//                    sheetState.hide()
//                    navigationHandler.navigate(
//                        NavigationAction.NavigateToComments(
//                            CommentType.Music,
//                            selectedSong!!.id
//                        )
//                    )
//                }
//            }) {
//                Text(text = "评论")
//            }
//
//            ListItem(icon = {
//                Icon(imageVector = Icons.Default.Comment, contentDescription = null)
//            }, modifier = Modifier.clickable {
//                scope.launch {
//                    sheetState.hide()
//                }
//                appViewModel.downloadMusic(selectedSong!!.id)
//            }) {
//
//
//                Text(text = "下载")
//            }
//
//            ListItem(icon = {
//                Icon(imageVector = Icons.Default.Share, contentDescription = null)
//            }, modifier = Modifier.clickable {
//
//                scope.launch {
//                    sheetState.hide()
//                    navigationHandler.navigate(
//                        NavigationAction.NavigateToShare(
//                            ShareType.Song,
//                            selectedSong!!.id,
//                            selectedSong.name,
//                            selectedSong.subTitle,
//                            selectedSong.album.imageUrl
//                        )
//                    )
//                }
//
//            }) {
//                Text(text = "分享")
//            }
//
//            selectedSong?.singers?.forEach {
//                ListItem(icon = {
//                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
//                }) {
//                    Text(text = "歌手：${it.name}", maxLines = 1)
//                }
//            }
//
//            selectedSong?.album?.run {
//                ListItem(icon = {
//                    Icon(imageVector = Icons.Default.Album, contentDescription = null)
//                }, modifier = Modifier.clickable {
//                    scope.launch {
//                        sheetState.hide()
//                        navigationHandler.navigate(NavigationAction.NavigateToAlbumDetail(id))
//                    }
//
//                }) {
//                    Text(text = "专辑：${name}", maxLines = 1)
//                }
//            }
//
//            actions()
//
//        }
//    }, sheetState = sheetState, content = content)
//}
//
//@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
//@Preview(showBackground = true, showSystemUi = true)
//@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
//@Composable
//fun SongBottomSheetLayoutPreview() {
//    ComposeMusicTheme {
//
//
//        val sheetState =
//            rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)
//        SongBottomSheetLayout(
//            selectedSong = mockSong,
//            sheetState = sheetState,
//            actions = { /*TODO*/ }) {
//            Scaffold(topBar = {
//                AppTopBar(title = { Text(text = "歌单") })
//            }) { paddingValues ->
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues)
//                ) {
//
//                }
//            }
//        }
//
//
//    }
//}