package com.ke.music.room.entity

import com.ke.music.common.entity.IArtist
import kotlinx.parcelize.Parcelize


@Parcelize
data class QueryHotArtistResult(
    val id: Long,
    override val artistId: Long,
    override val name: String,
    override val avatar: String?,
) : IArtist {
    override val key: Long
        get() = id
}
