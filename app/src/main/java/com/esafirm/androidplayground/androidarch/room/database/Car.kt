package com.esafirm.androidplayground.androidarch.room.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

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
