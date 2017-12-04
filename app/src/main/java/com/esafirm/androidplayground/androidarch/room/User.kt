package com.esafirm.androidplayground.androidarch.room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class User(@PrimaryKey(autoGenerate = false) val nik: Int,
           val name: String,
           val age: Int)
