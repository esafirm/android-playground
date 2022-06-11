package com.esafirm.androidplayground.securities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.common.MenuFactory

class SecurityMenuController : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return MenuFactory.create(
            container.context, listOf(
                "Bcrypt",
                "Facebook Conceal",
                "AES Encrypt Decrypt",
            )
        ) {
            router.pushController(
                RouterTransaction.with(
                    when (it) {
                        0 -> BcryptController()
                        1 -> ConcealController()
                        2 -> AesEncryptDecryptController()
                        else -> error("No controller for $it")
                    }
                )
            )
        }
    }
}
