package com.ke.music.room.entity

import com.ke.music.common.entity.IAlbum
import kotlinx.parcelize.Parcelize

@Parcelize
data class QueryNewAlbumResult(
    val id: Long,
    override val albumId: Long,
    override val name: String,
    override val image: String,
) : IAlbum {
    override val key: Long
        get() = id
}
