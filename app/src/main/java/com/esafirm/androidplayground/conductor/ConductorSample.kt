package com.esafirm.androidplayground.conductor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.changehandler.HorizontalChangeHandler
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.conductor.sharedtransition.SharedTransitionController
import com.esafirm.conductorextra.transaction.Routes
import java.io.Serializable
import java.util.*

class ConductorSample : Controller(), Serializable {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(container.context, Arrays.asList(
                "Configuration Change",
                "Shared Transition"
        )) { index ->
            when (index) {
                0 -> goToController(ConfigurationChangeController())
                1 -> goToController(SharedTransitionController())
            }
        }
    }

    private fun goToController(controller: Controller) {
        router.pushController(Routes.simpleTransaction(controller, HorizontalChangeHandler()))
    }
}
