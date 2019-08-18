package com.esafirm.androidplayground.network

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.MenuFactory

class NetworkSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(
                container.context,
                listOf("OkHttp - Upload Progress")
        ) { goToIndex(it) }
    }

    private fun goToIndex(index: Int) = start {
        when(index) {
            0 -> UploadProgressController()
            else -> throw IllegalStateException("Undefined index!")
        }
    }
}
