package com.esafirm.androidplayground.network

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.ControllerMaker
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.main.RouterAct
import java.util.*

class NetworkSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(container.context, Arrays.asList(
                "OkHttp - Upload Progress"
        ), { goToIndex(it) })
    }

    private fun goToIndex(index: Int) {
        when (index) {
            0 -> RouterAct.start(activity!!, ControllerMaker { UploadProgressController() })
        }
    }
}
