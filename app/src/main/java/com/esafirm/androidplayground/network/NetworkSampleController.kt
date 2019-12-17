package com.esafirm.androidplayground.network

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.navigateToController
import com.esafirm.androidplayground.utils.menu
import com.esafirm.androidplayground.utils.row

class NetworkSampleController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val items = listOf(
            "OkHttp - Upload Progress" navigateToController { UploadProgressController() },
            "Simple Network" navigateToController { SimpleNetworkController() }
        )
        return row {
            menu(items)
        }
    }
}
