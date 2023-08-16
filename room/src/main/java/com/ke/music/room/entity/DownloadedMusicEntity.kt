package com.ke.music.room.entity

import android.os.Parcelable
import com.ke.music.common.entity.DownloadStatus
import com.ke.music.common.entity.IAlbum
import com.ke.music.common.entity.IArtist
import com.ke.music.common.entity.ISong
import com.ke.music.common.entity.ISongEntity
import com.ke.music.room.db.entity.Album
import com.ke.music.room.db.entity.Music
import kotlinx.parcelize.Parcelize

@Parcelize
data class DownloadedMusicEntity(
    val musicId: Long,
    val name: String,
    override val path: String,
    val createdTime: Long,
    val mv: Long,
    val albumId: Long,
    val albumName: String,
    val albumImage: String,
) : Parcelable, ISongEntity {
    override val song: ISong
        get() = Music(musicId, name, albumId, mv)

    override val album: IAlbum
        get() = Album(albumId, albumName, albumImage)

    override val artists: List<IArtist>
        get() = emptyList()

    override val status: DownloadStatus
        get() = DownloadStatus.Downloaded
}
