package com.esafirm.androidplayground.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup

import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.common.BaseAct
import com.esafirm.androidplayground.common.ControllerMaker

class RouterAct : BaseAct() {

    private var router: Router? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_router)

        val maker = intent
                .extras
                .getSerializable(EXTRA_CONTROLLER) as ControllerMaker

        router = Conductor.attachRouter(this, findViewById<ViewGroup>(R.id.container), savedInstanceState)
        if (!router!!.hasRootController()) {
            router!!.setRoot(RouterTransaction.with(maker.makeController()))
        }
    }

    override fun onBackPressed() {
        if (!router!!.handleBack()) {
            super.onBackPressed()
        }
    }

    companion object {

        private const val EXTRA_CONTROLLER = "Extra.Controller"

        /* --------------------------------------------------- */
        /* > Stater */
        /* --------------------------------------------------- */

        fun start(context: Context, controller: ControllerMaker) {
            context.startActivity(Intent(context, RouterAct::class.java)
                    .putExtra(EXTRA_CONTROLLER, controller))
        }
    }
}
