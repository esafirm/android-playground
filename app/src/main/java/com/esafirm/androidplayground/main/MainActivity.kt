package com.esafirm.androidplayground.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.esafirm.androidplayground.androidarch.AndroidArchSampleController
import com.esafirm.androidplayground.anvil.AnvilSampleAct
import com.esafirm.androidplayground.common.ControllerMaker
import com.esafirm.androidplayground.common.MenuFactory
import com.esafirm.androidplayground.conductor.ConductorSample
import com.esafirm.androidplayground.dagger.example.DaggerSampleAct
import com.esafirm.androidplayground.kotlin.KotlinSampleController
import com.esafirm.androidplayground.network.NetworkSampleController
import com.esafirm.androidplayground.reductor.ReductorSampelAct
import com.esafirm.androidplayground.rxjava2.RxJava2SampleAct
import com.esafirm.androidplayground.securities.SecurityMenuController
import com.esafirm.androidplayground.ui.UISampleAct
import com.esafirm.androidplayground.utils.ActivityStater
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(
                MenuFactory.create(this,
                        Arrays.asList(
                                "Dagger Example",
                                "RxJava2 Example",
                                "UI Example",
                                "Reductor Sample",
                                "Conductor Sample",
                                "Security Sample",
                                "Anvil Sample (React Render Like)",
                                "Android Arch Sample",
                                "Kotlin Sample",
                                "Network Sample"
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
            4 -> RouterAct.start(this, ControllerMaker { ConductorSample() })
            5 -> RouterAct.start(this, ControllerMaker { SecurityMenuController() })
            6 -> RouterAct.start(this, ControllerMaker { AnvilSampleAct() })
            7 -> RouterAct.start(this, ControllerMaker { AndroidArchSampleController() })
            8 -> RouterAct.start(this, ControllerMaker { KotlinSampleController() })
            9 -> RouterAct.start(this, ControllerMaker { NetworkSampleController() })
        }
    }
}
