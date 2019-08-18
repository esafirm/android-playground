package com.esafirm.androidplayground.common

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.esafirm.androidplayground.utils.Logger

@SuppressLint("Registered")
open class BaseAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.clear()
    }
}
