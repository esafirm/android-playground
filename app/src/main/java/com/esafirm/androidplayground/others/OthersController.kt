package com.esafirm.androidplayground.others

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.common.navigateTo
import com.esafirm.androidplayground.utils.start

class OthersSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(
            container.context,
            listOf(
                "Leak Test" navigateTo { activity?.start<LeakTestActivity>() }
            )
        )
    }
}
