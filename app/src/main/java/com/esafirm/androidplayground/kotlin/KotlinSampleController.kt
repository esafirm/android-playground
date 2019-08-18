package com.esafirm.androidplayground.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.MenuFactory

class KotlinSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(container.context, listOf("Kotlin Type")) { goToIndex(it) }
    }

    private fun goToIndex(index: Int) = start {
        when (index) {
            0 -> KotlinTypeController()
            else -> throw IllegalStateException("Undefined index")
        }
    }

}
