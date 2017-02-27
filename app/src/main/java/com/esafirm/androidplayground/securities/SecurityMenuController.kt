package com.esafirm.androidplayground.securities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.MenuFactory
import java.util.*

class SecurityMenuController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(container.context, Arrays.asList(
                "Bcrypt",
                "Facebook Conceal"
        )) {
            router.pushController(RouterTransaction.with(
                    when (it) {
                        0 -> BcryptController()
                        else -> ConcealController()
                    }
            ))
        }
    }
}
