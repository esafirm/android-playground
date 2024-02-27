package com.esafirm.androidplayground.androidarch.room.database

import androidx.room.Embedded
import androidx.room.Relation

data class UserWitCars(
    @Embedded var user: User? = null,
    @Relation(parentColumn = "userId", entityColumn = "owner") var cars: List<Car>? = null
)
