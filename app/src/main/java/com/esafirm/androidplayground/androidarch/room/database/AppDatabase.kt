package com.esafirm.androidplayground.androidarch.room.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [User::class, Car::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun carDao(): CarDao

    companion object {
        fun get(context: Context) =
                Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "test.db")
                        .fallbackToDestructiveMigration()
                        .build()
    }
}
