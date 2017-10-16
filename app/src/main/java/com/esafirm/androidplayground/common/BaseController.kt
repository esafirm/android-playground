package com.esafirm.androidplayground.common

import com.bluelinelabs.conductor.Controller
import com.esafirm.androidplayground.utils.Logger

abstract class BaseController : Controller() {
    init {
        Logger.clear()
    }
}
