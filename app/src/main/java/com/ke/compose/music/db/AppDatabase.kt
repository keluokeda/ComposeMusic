package com.ke.compose.music.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Comment::class, ChildComment::class, User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun commentDao(): CommentDao

    abstract fun childCommentDao(): ChildCommentDao

    abstract fun userDao(): UserDao
}