package com.esafirm.androidplayground.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.ControllerMaker
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.main.RouterAct
import java.util.*

class KotlinSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(container.context, Arrays.asList(
                "Kotlin Type"
        ), { goToIndex(it) })
    }

    private fun goToIndex(index: Int) {
        when (index) {
            0 -> RouterAct.start(activity!!, ControllerMaker { KotlinTypeController() })
        }
    }

}
