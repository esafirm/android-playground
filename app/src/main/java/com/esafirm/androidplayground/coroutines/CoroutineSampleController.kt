package com.esafirm.androidplayground.coroutines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.MenuFactory

class CoroutineSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(container.context, listOf(
                "Behavior Subject",
                "Delay",
                "Blocking Controller"
        )) { goToIndex(it) }
    }

    private fun goToIndex(index: Int) = start {
        when (index) {
            0 -> BehaviorSubjectController()
            1 -> DelayController()
            2 -> UIBlockingCntroller()
            else -> throw IllegalStateException("Undefined index")
        }
    }
}