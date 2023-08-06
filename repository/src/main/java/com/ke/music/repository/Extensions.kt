package com.ke.music.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ke.music.api.response.Playlist
import com.ke.music.api.response.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

fun Playlist.convert(
    shareCount: Int = 0,
    bookedCount: Int = 0,
    commentCount: Int = 0
): com.ke.music.room.db.entity.Playlist {
    return com.ke.music.room.db.entity.Playlist(
        id,
        creator.userId,
        coverImgUrl,
        name,
        tags,
        description,
        trackCount,
        playCount,
        updateTime,
        shareCount, bookedCount, commentCount
    )
}

fun User.convert(): com.ke.music.room.db.entity.User {
    return com.ke.music.room.db.entity.User(userId, nickname, avatarUrl, signature)
}


// At the top level of your kotlin file:
private val Context.userIdStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
    name = "user_id"
)

private val KEY_USER_ID = longPreferencesKey("userId")


/**
 * 用户id的流
 */
val Context.userIdFlow: Flow<Long>
    get() = userIdStore.data.map {

        it[KEY_USER_ID] ?: 0L
    }

suspend fun Context.getUserId() = userIdFlow.firstOrNull() ?: 0L


/**
 * 设置用户id
 */
suspend fun Context.setUserId(userId: Long) {
    userIdStore.edit {
        it[KEY_USER_ID] = userId
    }
}