package com.esafirm.androidplayground.conductor

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.esafirm.androidplayground.R
import com.esafirm.androidplayground.libs.Logger
import com.esafirm.conductorextra.butterknife.BinderController
import com.esafirm.conductorextra.isMarkedSavedState

class ConfigurationChangeController : BinderController(), ConfigurationChangeControllerView {

    private val presenter: ConfigurationChangeControllerPresenter
            by lazy { ConfigurationChangeControllerPresenter().apply { view = this@ConfigurationChangeController } }

    init {
        attachLifecycleLogger()
    }

    override fun getLayoutResId(): Int = R.layout.activity_router

    override fun onViewBound(bindingResult: View, savedState: Bundle?) {
        bindingResult.let { it as ViewGroup }
                .addView(Logger.getLogView(bindingResult.context))

        if (!savedState.isMarkedSavedState()) {
            Logger.log("-> Fetch ~")
            presenter.fetch()
        }
    }

    /* --------------------------------------------------- */
    /* > Activities */
    /* --------------------------------------------------- */

    override fun onActivityStarted(activity: Activity) {
        super.onActivityStarted(activity)
        Logger.log("-> onActivityStarted")
    }

    /* --------------------------------------------------- */
    /* > Instance State */
    /* --------------------------------------------------- */

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Logger.log("$ onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Logger.log("$ onRestoreInstanceState state:$savedInstanceState")
    }

    /* --------------------------------------------------- */
    /* > View State */
    /* --------------------------------------------------- */

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        outState.putBoolean("CONFIG_CHANGE", true)
        Logger.log("-> onSaveViewState")
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        super.onRestoreViewState(view, savedViewState)
        Logger.log("-> onRestoreViewState state:$savedViewState")
    }

    /* --------------------------------------------------- */
    /* > View Methods */
    /* --------------------------------------------------- */

    override fun showResult() {
        Logger.log("Showing result……")
    }

    private fun attachLifecycleLogger() {
        addLifecycleListener(object : LifecycleListener() {
            override fun preAttach(controller: Controller, view: View) {
                Logger.log("$ pre attach")
            }

            override fun postAttach(controller: Controller, view: View) {
                Logger.log("$ post attach")
            }

            override fun preCreateView(controller: Controller) {
                Logger.log("$ pre create view")
            }

            override fun postCreateView(controller: Controller, view: View) {
                Logger.log("$ post create view")
            }

            override fun preDetach(controller: Controller, view: View) {
                Logger.log("$ pre detach")
            }

            override fun preDestroy(controller: Controller) {
                Logger.log("$ pre destroy")
            }

            override fun preDestroyView(controller: Controller, view: View) {
                Logger.log("$ pre destroy view")
            }
        })
    }
}
