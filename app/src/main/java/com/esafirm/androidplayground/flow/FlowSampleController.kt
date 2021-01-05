package com.esafirm.androidplayground.flow

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.MenuFactory

class FlowSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(container.context, listOf(
            "Flow - Click Binding",
        )) { goToIndex(it) }
    }

    private fun goToIndex(index: Int) = start {
        when (index) {
            0 -> FlowBindingController()
            else -> throw IllegalStateException("Undefined index!")
        }
    }
}