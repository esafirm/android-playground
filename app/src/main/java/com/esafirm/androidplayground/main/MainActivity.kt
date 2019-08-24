package com.esafirm.androidplayground.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.esafirm.androidplayground.androidarch.AndroidArchSampleController
import com.esafirm.androidplayground.anvil.AnvilSampleAct
import com.esafirm.androidplayground.common.ControllerMaker
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.common.navigateTo
import com.esafirm.androidplayground.conductor.ConductorSample
import com.esafirm.androidplayground.coroutines.CoroutineSampleController
import com.esafirm.androidplayground.dagger.example.DaggerSampleAct
import com.esafirm.androidplayground.flipper.FlipperController
import com.esafirm.androidplayground.kotlin.KotlinSampleController
import com.esafirm.androidplayground.network.NetworkSampleController
import com.esafirm.androidplayground.others.OthersSampleController
import com.esafirm.androidplayground.reductor.ReductorSampelAct
import com.esafirm.androidplayground.rxjava2.RxJava2SampleAct
import com.esafirm.androidplayground.securities.SecurityMenuController
import com.esafirm.androidplayground.startup.STracker
import com.esafirm.androidplayground.ui.UISampleAct
import com.esafirm.androidplayground.utils.ActivityStater

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        STracker.end()

        val items = listOf(
            "Dagger Example" navigateTo { DaggerSampleAct.start(this) },
            "RxJava2 Example" navigateTo { RxJava2SampleAct.start(this) }
        )

        setContentView(
            MenuFactory.create(this,
                listOf(
                    "Dagger Example",
                    "RxJava2 Example",
                    "UI Example",
                    "Reductor Sample",
                    "Conductor Sample",
                    "Coroutine Sample",
                    "Security Sample",
                    "Anvil Sample (React Render Like)",
                    "Android Arch Sample",
                    "Kotlin Sample",
                    "Network Sample",
                    "Flipper",
                    "Others"
                )
            ) { index -> navigateToPage(index) }
        )
    }

    private fun navigateToPage(index: Int) {
        when (index) {
            0 -> DaggerSampleAct.start(this)
            1 -> RxJava2SampleAct.start(this)
            2 -> ActivityStater.start(this, UISampleAct::class.java)
            3 -> ActivityStater.start(this, ReductorSampelAct::class.java)
            else -> startController(index)
        }
    }

    private fun startController(index: Int) = RouterAct.start(this, ControllerMaker {
        when (index) {
            4 -> ConductorSample()
            5 -> CoroutineSampleController()
            6 -> SecurityMenuController()
            7 -> AnvilSampleAct()
            8 -> AndroidArchSampleController()
            9 -> KotlinSampleController()
            10 -> NetworkSampleController()
            11 -> FlipperController()
            12 -> OthersSampleController()
            else -> throw IllegalStateException("Undefined index!")
        }
    })
}
