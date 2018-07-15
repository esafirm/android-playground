package com.esafirm.androidplayground.androidarch.room.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface CarDao {
    @Insert
    fun insertCar(car: Car)

    @Query("SELECT * FROM car")
    fun getAllData(): List<Car>

    @Query("DELETE FROM car")
    fun deleteAll()

    @Query("SELECT COUNT(carId) FROM car")
    fun getCarCount(): Int
}
