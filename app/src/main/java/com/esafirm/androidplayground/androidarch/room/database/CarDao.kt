package com.esafirm.androidplayground.androidarch.room.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

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
