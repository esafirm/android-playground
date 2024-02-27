package com.esafirm.androidplayground.androidarch.room.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "car",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["userId"],
        childColumns = ["owner"]
    )]
)
data class Car(
    @PrimaryKey(autoGenerate = true)
    val carId: Int? = null,
    val name: String,
    val owner: Int
)
