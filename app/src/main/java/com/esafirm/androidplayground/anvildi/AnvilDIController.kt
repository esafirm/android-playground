package com.esafirm.androidplayground.anvildi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import com.esafirm.printer.FeaturePrintComponent
import com.google.android.gms.common.Feature

class AnvilDIController : BaseController() {

    private val component by lazy {
        DaggerAnvilAppComponent.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            button("Print Data") {
                (component as FeaturePrintComponent).printer().print()
                Logger.log("Data printed. Check log")
            }
            logger()
        }
    }
}
