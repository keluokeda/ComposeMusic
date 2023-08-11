package com.ke.music.room.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("all_mv")
data class AllMv(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo("mv_id")
    val mvId: Long,

    val area: String,

    val type: String
)
