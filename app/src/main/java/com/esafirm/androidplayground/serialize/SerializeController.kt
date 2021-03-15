package com.esafirm.androidplayground.serialize

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.navigateToController
import com.esafirm.androidplayground.utils.menu

class SerializeController : BaseController() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return menu(
            "Object Serialization" navigateToController { ObjectSerializationController() }
        )
    }
}