package com.esafirm.androidplayground.androidarch.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class User(
        @PrimaryKey(autoGenerate = true) val userId: Int? = null,
        val name: String,
        val age: Int
)
