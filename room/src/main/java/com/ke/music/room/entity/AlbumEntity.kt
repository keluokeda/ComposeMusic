package com.ke.music.room.entity

data class AlbumEntity(
    val albumId: Long,
    val name: String,
    val image: String,
    val description: String?,
    val artistId: Long,
    val artistName: String,
    val index: Int?
) {
    val collected: Boolean
        get() = index != null
}
