package com.ke.music.common.entity

interface IAlbumEntity {
    val album: IAlbum

    /**
     * 描述
     */
    val description: String?

    /**
     * 主歌手
     */
    val artist: IArtist

    /**
     * 是否收藏
     */
    val collected: Boolean
}