package com.ke.music.room.db

import androidx.room.TypeConverter

class Converts {

    @TypeConverter
    fun stringToList(string: String): List<String> {
        return string.split("-")
    }

    @TypeConverter
    fun listToString(list: List<String>): String {
        return list.joinToString("-")
    }
}