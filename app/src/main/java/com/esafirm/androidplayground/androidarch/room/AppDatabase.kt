package com.esafirm.androidplayground.androidarch.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(User::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
