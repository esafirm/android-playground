package com.esafirm.androidplayground.flow

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.libs.Logger
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

@ExperimentalCoroutinesApi
class FlowBindingController : BaseController() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            val button1 = Button(context).apply {
                text = "Button #1"
            }
            val button2 = Button(context).apply {
                text = "Button #2"
            }

            CoroutineScope(Dispatchers.Main).launch {
                merge(
                    button1.clicks(),
                    button2.clicks()
                ).stateIn(this).onEach {
                    // Simulate error
                    if (Random.nextBoolean()) {
                        error("Somethings wrong I can feel it")
                    }
                }.catch {
                    Logger.log("Error $it")
                }.collect {
                    Logger.log("Click by ${(it as Button).text}")
                }
            }

            addView(button1)
            addView(button2)

            logger()
        }
    }

    private fun View.clicks(): Flow<View> = callbackFlow {
        this@clicks.setOnClickListener {
            this.offer(it)
        }
        awaitClose { this@clicks.setOnClickListener(null) }
    }

}
