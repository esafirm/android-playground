package com.esafirm.androidplayground

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.esafirm.androidplayground.dagger.AppComponent
import com.esafirm.androidplayground.flipper.FlipperWrapper
import com.esafirm.androidplayground.startup.STracker
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary
import java.util.prefs.Preferences


class PlaygroundApp : Application() {

    init {
        component = AppComponent.Initializer.make(this)
    }

    override fun onCreate() {
        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)

        STracker.appOnCreate()

        Stetho.initializeWithDefaults(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initFlipper()
    }

    private fun initFlipper() {
       FlipperWrapper.setup(this)
    }

    companion object {

        private lateinit var component: AppComponent

        fun component(): AppComponent {
            return component
        }

        fun appContext(): Context {
            return component.appContext()
        }
    }
}
