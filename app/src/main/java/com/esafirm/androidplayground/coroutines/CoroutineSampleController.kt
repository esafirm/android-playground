package com.esafirm.androidplayground.coroutines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.ControllerMaker
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.main.RouterAct

class CoroutineSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(container.context, listOf(
                "Behavior Subject",
                "Delay"
        )) { goToIndex(it) }
    }

    private fun goToIndex(index: Int) {
        when (index) {
            0 -> RouterAct.start(activity!!, ControllerMaker { BehaviorSubjectController() })
            1 -> RouterAct.start(activity!!, ControllerMaker { DelayController() })
        }
    }
}