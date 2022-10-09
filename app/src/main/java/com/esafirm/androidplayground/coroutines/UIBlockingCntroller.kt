package com.esafirm.androidplayground.coroutines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.libs.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import kotlinx.coroutines.*

class UIBlockingCntroller : BaseController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View =
        row {
            button("Start Coroutine") {
                startCoroutine()
            }
            logger()
        }

    private fun startCoroutine() = GlobalScope.launch {
        Logger.log("Before hello")
        val somethingSlow = withContext(Dispatchers.Main) {
            delay(5000)
            "Hello World"
        }
        Logger.log(somethingSlow)
    }
}
