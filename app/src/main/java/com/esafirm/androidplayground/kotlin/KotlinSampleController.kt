package com.esafirm.androidplayground.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.MenuFactory

class KotlinSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(container.context, listOf(
            "Kotlin Type",
            "Top File Lazy Test",
            "Class Loader Duration"
        )) { goToIndex(it) }
    }

    private fun goToIndex(index: Int) = start {
        when (index) {
            0 -> KotlinTypeController()
            1 -> TopFileLazyController()
            2 -> ClassLoaderCheckController()
            else -> throw IllegalStateException("Undefined index")
        }
    }

}
