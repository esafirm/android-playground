package com.esafirm.androidplayground.firebase

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.navigateToController
import com.esafirm.androidplayground.utils.menu

class FirebaseController : BaseController() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return menu(
            "Remote config" navigateToController { RemoteConfigController() }
        )
    }
}