package com.esafirm.androidplayground.common

import com.bluelinelabs.conductor.Controller
import com.esafirm.androidplayground.main.RouterAct
import com.esafirm.androidplayground.utils.Logger

abstract class BaseController : Controller() {
    init {
        Logger.clear()
    }

    val requiredContext get() = activity!!

    fun start(maker: () -> Controller) =
            RouterAct.start(requiredContext, ControllerMaker(maker))
}
