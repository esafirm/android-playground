package com.esafirm.androidplayground.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row

class TopFileLazyController : BaseController() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            button("Check Top File") {
                Logger.log(someValue)
            }
            logger()
        }
    }
}