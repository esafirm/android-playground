package com.esafirm.androidplayground.provider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.ProcessUtils
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row

class ProcessInfoController : BaseController() {
    override fun onCreateView(p0: LayoutInflater, p1: ViewGroup): View {
        return row {
            logger()
            button("Check process") {
                Toast.makeText(
                    applicationContext,
                    ProcessUtils.getCurrentProcessName(requiredContext),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}