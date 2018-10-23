package com.esafirm.androidplayground.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.esafirm.androidplayground.common.ControllerMaker
import com.esafirm.androidplayground.main.RouterAct

inline fun <reified T : Activity> Context.start() {
    startActivity(Intent(this, T::class.java))
}

fun Activity.routerStart(controller: ControllerMaker) {
    RouterAct.start(this, controller)
}
