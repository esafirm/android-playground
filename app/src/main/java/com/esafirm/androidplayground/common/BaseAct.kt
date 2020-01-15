package com.esafirm.androidplayground.common

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.esafirm.androidplayground.utils.ContextProvider

import com.esafirm.androidplayground.utils.Logger

@SuppressLint("Registered")
open class BaseAct : AppCompatActivity() , ContextProvider{

    override val requiredContext: Context
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.clear()
    }
}
