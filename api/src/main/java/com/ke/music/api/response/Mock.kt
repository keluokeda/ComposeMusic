package com.ke.music.api.response

val mockUser = User(
    userId = 100,
    signature = "发掘最好听的艺人，锁定@云音乐艺人精选！",
    nickname = "汉库克",
    followed = true,
    avatarUrl = "http://p1.music.126.net/PaTS-XnE6BOskByBuDfPHg==/109951168274670299.jpg"
)

val mockUserList = listOf(
    mockUser,
    mockUser.copy(userId = 101),
    mockUser.copy(userId = 102),
    mockUser.copy(userId = 103),
    mockUser.copy(userId = 104),
    mockUser.copy(userId = 105),
)

val mockPlaylist = Playlist(
    100,
    mockUser,
    "",
    "最爱邓丽君",
    listOf("国语", "经典"),
    "发掘最好听的艺人，锁定@云音乐艺人精选！",
    trackCount = 120,
    playCount = 100000,
    subscribers = mockUserList
)

val mockSong =
    Song(10, "甜蜜蜜", Album(10, "漫漫人生路", ""), listOf(Singer(10, "邓丽君")), 10)

val mockSongList = listOf(
    mockSong,
    mockSong.copy(id = 11),
    mockSong.copy(id = 12),
    mockSong.copy(id = 13),
    mockSong.copy(id = 14),
)

val mockPrivateMessage = PrivateMessage(
    lastMessage = "{\"msg\":\"好吧\",\"type\":6,\"title\":\"\"}",
    time = 1682220697010L,
    toUser = mockUser,
    fromUser = mockUser,
    id = 10
)
