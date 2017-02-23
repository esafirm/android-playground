package com.esafirm.androidplayground.common

import com.bluelinelabs.conductor.Controller

import java.io.Serializable

class ControllerMaker(val maker: () -> Controller) : Serializable {
    fun makeController(): Controller = maker()
}
