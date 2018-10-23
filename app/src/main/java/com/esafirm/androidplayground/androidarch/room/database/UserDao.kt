package com.esafirm.androidplayground.androidarch.room.database

import androidx.room.*

@Dao
abstract class UserDao {
    @Query("SELECT * FROM user")
    abstract fun getUsers(): List<User>

    @Query("SELECT * FROM user WHERE age = :age")
    abstract fun getUserWithAge(age: Int): List<User>

    @Delete
    abstract fun deleteUsers(vararg users: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(user: User): Long

    @Query("SELECT * FROM user")
    abstract fun getUserWithCars(): List<UserWitCars>

    @Query("SELECT u.* FROM user u JOIN car c WHERE c.name = :name")
    abstract fun getUsersWithCar(name: String): List<User>

    @Transaction
    open fun replaceUser(old: User, new: User) {
        insertUser(new)
        deleteUsers(old)
    }

    @Transaction
    open fun replaceUserWithError(old: User, new: User) {
        insertUser(new)
        if (true) {
            throw IllegalStateException()
        }
        deleteUsers(old)
    }
}
