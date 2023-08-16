package com.ke.music.room.entity

import android.os.Parcelable
import com.ke.music.common.entity.IAlbum
import com.ke.music.common.entity.IAlbumEntity
import com.ke.music.common.entity.IArtist
import com.ke.music.room.db.entity.Album
import com.ke.music.room.db.entity.Artist
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumEntity(
    val albumId: Long,
    val name: String,
    val image: String,
    override val description: String?,
    val artistId: Long,
    val artistName: String,
    val index: Int?,
) : Parcelable, IAlbumEntity {
    override val collected: Boolean
        get() = index != null

    override val album: IAlbum
        get() = Album(albumId, name, image)

    override val artist: IArtist
        get() = Artist(artistId, artistName, null)
}
