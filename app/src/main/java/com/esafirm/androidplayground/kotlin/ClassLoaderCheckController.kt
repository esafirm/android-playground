package com.esafirm.androidplayground.kotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.esafirm.androidplayground.common.BaseController
import com.esafirm.androidplayground.libs.Logger
import com.esafirm.androidplayground.utils.button
import com.esafirm.androidplayground.utils.logger
import com.esafirm.androidplayground.utils.row
import kotlin.system.measureTimeMillis

class ClassLoaderCheckController : BaseController() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return row {
            button("Try Load Class") {
                tryLoadClass()
            }
            logger()
        }
    }

    private fun tryLoadClass() {
        val time = measureTimeMillis {
            Class.forName("com.esafirm.androidplayground.kotlin.KotlinSampleController")
        }

        val emptyTime = measureTimeMillis {
            try {
                Class.forName("androidx.test.espresso.Espresso")
            } catch (e: Exception) {
                Logger.log("Class not found")
            }
        }

        Logger.log("""
            Time: $time ms
            Empty Time: $emptyTime ms
        """.trimIndent())
    }
}
