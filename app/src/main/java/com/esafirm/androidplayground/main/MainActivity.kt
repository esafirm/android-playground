package com.esafirm.androidplayground.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.esafirm.androidplayground.androidarch.AndroidArchSampleController
import com.esafirm.androidplayground.anvil.AnvilSampleAct
import com.esafirm.androidplayground.anvildi.AnvilDIController
import com.esafirm.androidplayground.common.navigateTo
import com.esafirm.androidplayground.common.navigateToController
import com.esafirm.androidplayground.conductor.ConductorSample
import com.esafirm.androidplayground.coroutines.CoroutineSampleController
import com.esafirm.androidplayground.dagger.example.DaggerSampleController
import com.esafirm.androidplayground.ffmpeg.FfmpegController
import com.esafirm.androidplayground.firebase.FirebaseController
import com.esafirm.androidplayground.flipper.FlipperController
import com.esafirm.androidplayground.flow.FlowSampleController
import com.esafirm.androidplayground.fragment.ViewPagerCurrentFragmentSampleActivity
import com.esafirm.androidplayground.images.ShowImageController
import com.esafirm.androidplayground.kotlin.KotlinSampleController
import com.esafirm.androidplayground.network.NetworkSampleController
import com.esafirm.androidplayground.others.OthersSampleController
import com.esafirm.androidplayground.provider.ProcessInfoController
import com.esafirm.androidplayground.reductor.ReductorSampelAct
import com.esafirm.androidplayground.rn.ReactActivity
import com.esafirm.androidplayground.rxjava2.RxJava2SampleController
import com.esafirm.androidplayground.securities.SecurityMenuController
import com.esafirm.androidplayground.serialize.SerializeController
import com.esafirm.androidplayground.service.SampleService
import com.esafirm.androidplayground.startup.STracker
import com.esafirm.androidplayground.ui.UISampleAct
import com.esafirm.androidplayground.utils.ActivityStater
import com.esafirm.androidplayground.utils.ContextProvider
import com.esafirm.androidplayground.utils.input
import com.esafirm.androidplayground.utils.menu
import com.esafirm.androidplayground.utils.row
import com.esafirm.androidplayground.webview.VasSonicController
import java.util.Locale

class MainActivity : AppCompatActivity(), ContextProvider {

    override val requiredContext: Context
        get() = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        STracker.end()

        startService(Intent(this, SampleService::class.java))

        val items = listOf(
            "Dagger Example" navigateToController { DaggerSampleController() },
            "RxJava2 Example" navigateToController { RxJava2SampleController() },
            "UI Example" navigateTo { ActivityStater.start(this, UISampleAct::class.java) },
            "Reductor" navigateTo { ActivityStater.start(this, ReductorSampelAct::class.java) },
            "Conductor" navigateToController { ConductorSample() },
            "Coroutine" navigateToController { CoroutineSampleController() },
            "Firebase" navigateToController { FirebaseController() },
            "FFmpeg" navigateToController { FfmpegController() },
            "Fragment Samples" navigateTo { ViewPagerCurrentFragmentSampleActivity.start(this) },
            "Security Samples" navigateToController { SecurityMenuController() },
            "Anvil Sample (React Render Like)" navigateToController { AnvilSampleAct() },
            "Anvil DI (Dagger Support)" navigateToController { AnvilDIController() },
            "Android Arch Sample" navigateToController { AndroidArchSampleController() },
            "Kotlin Sample" navigateToController { KotlinSampleController() },
            "Kotlin Flow" navigateToController { FlowSampleController() },
            "Network Sample" navigateToController { NetworkSampleController() },
            "Flipper" navigateToController { FlipperController() },
            "React Native" navigateTo { ReactActivity.start(this) },
            "Serialization Sample" navigateToController { SerializeController() },
            "VasSonic" navigateToController { VasSonicController() },
            "Others" navigateToController { OthersSampleController() },
            "Process Info" navigateToController { ProcessInfoController() },
            "Images" navigateToController { ShowImageController() }
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
