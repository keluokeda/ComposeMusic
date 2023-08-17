package com.ke.music.common.entity

interface ISongEntity {
    val song: ISong
    val album: IAlbum
    val artists: List<IArtist>

    /**
     * 下载状态
     */
    val status: DownloadStatus

    /**
     * 本地路径 已下载才有
     */
    val path: String?


    /**
     * 副标题
     */
    fun subtitle(): String {
        return artists.joinToString("-") { it.name } + " " + album.name
    }
}

