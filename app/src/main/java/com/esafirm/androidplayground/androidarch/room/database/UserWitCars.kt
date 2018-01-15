package com.esafirm.androidplayground.androidarch.room.database

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

data class UserWitCars(
        @Embedded var user: User? = null,
        @Relation(parentColumn = "userId", entityColumn = "owner") var cars: List<Car>? = null
)
