package com.esafirm.androidplayground.anvil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.MenuFactory
import java.util.*

class AnvilSampleAct : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
            MenuFactory.create(container.context, Arrays.asList(
                    "Simple Anvil"
            ), { navigateToIndex(it) })

    fun navigateToIndex(index: Int) {
        router.pushController(RouterTransaction.with(AnvilController())
                .popChangeHandler(HorizontalChangeHandler())
                .pushChangeHandler(HorizontalChangeHandler()))
    }
}