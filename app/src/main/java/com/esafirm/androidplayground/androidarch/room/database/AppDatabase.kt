package com.esafirm.androidplayground.androidarch.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

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
