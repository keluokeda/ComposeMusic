package com.ke.compose.music.ui.component

//
//@Composable
//fun MusicView(
//    musicEntity: MusicEntity,
//    rightButton: @Composable () -> Unit = {
//    },
//    onClick: (MusicEntity, MusicPlayerController) -> Unit = { entity, controller ->
//        controller.playMusic(entity.musicId)
//    }
//) {
//
//    val size = 40.dp
//    Column {
//        val controller = LocalMusicPlayerController.current
//        Row(
//            modifier = Modifier
//                .clickable {
//                    onClick(musicEntity, controller)
//                }
//                .padding(horizontal = 16.dp, vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//
//
//            AsyncImage(
//                model = musicEntity.album.imageUrl,
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .size(size)
//                    .background(MaterialTheme.colorScheme.primary)
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//            Column(modifier = Modifier.weight(1f)) {
//                Text(text = musicEntity.name, maxLines = 1)
//                Text(
//                    text = "${musicEntity.artists.joinToString("/") { it.name }}-${musicEntity.album.name}",
//                    style = MaterialTheme.typography.bodySmall,
//                    maxLines = 1
//                )
//            }
//
//
//            if (musicEntity.downloadStatus != null) {
//
//                val imageVector = when (musicEntity.downloadStatus) {
//                    Download.STATUS_DOWNLOADED -> Icons.Default.DownloadDone
//                    else -> Icons.Default.Downloading
//                }
//
//                IconButton(onClick = { }, enabled = false) {
//                    Icon(imageVector = imageVector, contentDescription = null)
//                }
//            }
//
//            if (musicEntity.mv != 0L) {
//                IconButton(onClick = { }) {
//                    Icon(imageVector = Icons.Default.PlayCircle, contentDescription = null)
//
//                }
//            }
//
//            MusicDropDownMenu(false, musicEntity)
//        }
//        Divider(modifier = Modifier.height(0.2.dp))
//    }
//}
//
//@Composable
//private fun MusicDropDownMenu(
//    initialExpanded: Boolean = false,
//    musicEntity: MusicEntity
//) {
//    var expanded by remember {
//        mutableStateOf(initialExpanded)
//    }
//    Box {
//        IconButton(onClick = {
//            expanded = true
//        }) {
//            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
//        }
//
//        val navigationHandler = LocalNavigationHandler.current
//
//        val downloadManager = LocalDownloadManager.current
//
//        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//            DropdownMenuItem(text = {
//                Text(text = "下一首播放")
//            }, onClick = {
//                expanded = false
//            })
//
//            DropdownMenuItem(text = {
//                Text(text = "收藏到歌单")
//            }, onClick = {
//                expanded = false
//            })
//
//            DropdownMenuItem(text = {
//                Text(text = "评论")
//            }, onClick = {
//                navigationHandler.navigate(
//                    NavigationAction.NavigateToComments(
//                        CommentType.Music,
//                        musicEntity.musicId
//                    )
//                )
//                expanded = false
//            })
//
//            if (musicEntity.downloadStatus != Download.STATUS_DOWNLOADED) {
//
//                DropdownMenuItem(text = {
//                    Text(text = "下载")
//                }, onClick = {
//                    expanded = false
//                    downloadManager.downloadMusic(musicEntity.musicId)
//                })
//            }
//
//            DropdownMenuItem(text = {
//                Text(text = "分享")
//            }, onClick = {
//                expanded = false
//                navigationHandler.navigate(
//                    NavigationAction.NavigateToShare(
//                        ShareType.Song,
//                        musicEntity.musicId,
//                        musicEntity.name,
//                        musicEntity.subTitle,
//                        musicEntity.album.imageUrl
//                    )
//                )
//            })
//
//            musicEntity.artists.forEach {
//                DropdownMenuItem(text = {
//                    Text(text = "歌手：${it.name}")
//                }, onClick = {
//                    expanded = false
//                    navigationHandler.navigate(NavigationAction.NavigateToArtistDetail(it.artistId))
//                })
//            }
//
//            DropdownMenuItem(text = {
//                Text(text = "专辑：${musicEntity.album.name}")
//            }, onClick = {
//                expanded = false
//                navigationHandler.navigate(NavigationAction.NavigateToAlbumDetail(musicEntity.album.albumId))
//            })
//        }
//    }
//}
//
//
//@Preview
//@Composable
//private fun MusicDropDownMenuExpandedTrue() {
//    ComposeMusicTheme {
//        MusicDropDownMenu(true, musicEntity)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MusicViewIdlePreview() {
//    ComposeMusicTheme {
//        MusicView(status = Download.STATUS_DOWNLOAD_IDLE)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MusicViewDownloadingPreview() {
//    ComposeMusicTheme {
//        MusicView(status = Download.STATUS_DOWNLOADING)
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun MusicViewDownloadedPreview() {
//    ComposeMusicTheme {
//        MusicView(status = Download.STATUS_DOWNLOADED, mv = 1L)
//    }
//}
//
//@Composable
//private fun MusicView(status: Int, mv: Long = 0) {
//
//
//    MusicView(musicEntity = musicEntity, rightButton = {})
//}
//
//private val musicEntity = MusicEntity(
//    musicId = 0L,
//    name = "漫步人生路",
//    mv = 0,
//    album = Album(0L, "最爱", ""),
//    artists = listOf(
//        Artist(0, "邓丽君")
//    ),
//    downloadStatus = 0
//)