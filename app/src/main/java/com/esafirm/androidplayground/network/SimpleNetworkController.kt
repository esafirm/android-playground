package com.esafirm.androidplayground.network

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.network.services.DogService
import com.esafirm.androidplayground.libs.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SimpleNetworkController : BaseController() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            button("Do Network Request") {
                doNetworkRequest()
            }
            logger()
        }
    }

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    private fun doNetworkRequest() = GlobalScope.launch(errorHandler) {
        Logger.log("Do network request …")
        val results = Net.getService(DogService::class.java).getRandomDogs()
        Logger.log("Get results : $results")
    }
}
