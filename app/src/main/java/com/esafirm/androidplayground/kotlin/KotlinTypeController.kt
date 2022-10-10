package com.esafirm.androidplayground.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.esafirm.androidplayground.libs.Logger

class KotlinTypeController : Controller() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            Logger.getLogView(container.context)

    override fun onAttach(view: View) {
        super.onAttach(view)
        Logger.log("Assert that ${Unit is Nothing}")
    }
}
