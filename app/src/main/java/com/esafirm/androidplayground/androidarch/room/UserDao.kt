package com.esafirm.androidplayground.androidarch.room

import android.arch.persistence.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUsers(): List<User>

    @Delete
    fun deleteUsers(vararg users: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)
}
