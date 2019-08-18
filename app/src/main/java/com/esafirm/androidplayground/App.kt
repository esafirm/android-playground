package com.esafirm.androidplayground

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.esafirm.androidplayground.dagger.AppComponent
import com.esafirm.androidplayground.startup.STracker
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary


class App : Application() {
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
        SoLoader.init(this, false)
        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.start()
        }
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
