package com.esafirm.androidplayground.coroutines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.common.navigateToController

class CoroutineSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(container.context, listOf(
            "Behavior Subject" navigateToController { BehaviorSubjectController() },
            "Delay" navigateToController { DelayController() },
            "Blocking Controller" navigateToController { UIBlockingCntroller() },
            "Unconfined Test" navigateToController { UnconfinedTestController() }
        ))
    }
}