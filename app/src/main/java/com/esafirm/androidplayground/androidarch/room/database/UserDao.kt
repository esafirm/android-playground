package com.esafirm.androidplayground.androidarch.room.database

import android.arch.persistence.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUsers(): List<User>

    @Query("SELECT * FROM user WHERE age = :age")
    fun getUserWithAge(age: Int): List<User>

    @Delete
    fun deleteUsers(vararg users: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("SELECT * FROM user")
    fun getUserWithCars(): List<UserWitCars>
}
