package com.esafirm.androidplayground.main

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.esafirm.androidplayground.androidarch.AndroidArchSampleController
import com.esafirm.androidplayground.anvil.AnvilSampleAct
import com.esafirm.androidplayground.common.navigateTo
import com.esafirm.androidplayground.common.navigateToController
import com.esafirm.androidplayground.conductor.ConductorSample
import com.esafirm.androidplayground.coroutines.CoroutineSampleController
import com.esafirm.androidplayground.dagger.example.DaggerSampleAct
import com.esafirm.androidplayground.flipper.FlipperController
import com.esafirm.androidplayground.kotlin.KotlinSampleController
import com.esafirm.androidplayground.network.NetworkSampleController
import com.esafirm.androidplayground.others.OthersSampleController
import com.esafirm.androidplayground.provider.ProcessInfoController
import com.esafirm.androidplayground.reductor.ReductorSampelAct
import com.esafirm.androidplayground.rn.ReactActivity
import com.esafirm.androidplayground.rxjava2.RxJava2SampleAct
import com.esafirm.androidplayground.securities.SecurityMenuController
import com.esafirm.androidplayground.startup.STracker
import com.esafirm.androidplayground.ui.UISampleAct
import com.esafirm.androidplayground.utils.*
import com.esafirm.androidplayground.webview.VasSonicController
import java.util.*

class MainActivity : AppCompatActivity(), ContextProvider {

    override val requiredContext: Context
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        STracker.end()

        val items = listOf(
            "Dagger Example" navigateTo { DaggerSampleAct.start(this) },
            "RxJava2 Example" navigateTo { RxJava2SampleAct.start(this) },
            "UI Example" navigateTo { ActivityStater.start(this, UISampleAct::class.java) },
            "Reductor" navigateTo { ActivityStater.start(this, ReductorSampelAct::class.java) },
            "Conductor" navigateToController { ConductorSample() },
            "Coroutine" navigateToController { CoroutineSampleController() },
            "Security Sampels" navigateToController { SecurityMenuController() },
            "Anvil Sample (React Render Like)" navigateToController { AnvilSampleAct() },
            "Android Arch Sample" navigateToController { AndroidArchSampleController() },
            "Kotlin Sample" navigateToController { KotlinSampleController() },
            "Network Sample" navigateToController { NetworkSampleController() },
            "Flipper" navigateToController { FlipperController() },
            "React Native" navigateTo { ReactActivity.start(this) },
            "VasSonic" navigateToController { VasSonicController() },
            "Others" navigateToController { OthersSampleController() },
            "Process Info" navigateToController { ProcessInfoController() }
        )

        setContentView(row {
            input("Search item here…") { query ->
                menu(items.filter {
                    it.name.toLowerCase(Locale.getDefault())
                        .contains(query.toLowerCase(Locale.getDefault()))
                })
            }
        })
    }
}
