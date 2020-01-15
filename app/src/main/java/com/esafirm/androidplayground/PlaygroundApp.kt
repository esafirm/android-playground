package com.esafirm.androidplayground

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.esafirm.androidplayground.dagger.AppComponent
import com.esafirm.androidplayground.startup.STracker
import com.esafirm.androidplayground.utils.Logger
import com.esafirm.androidplayground.utils.ProcessUtils
import com.facebook.stetho.Stetho

class PlaygroundApp : Application() {

    init {
        component = AppComponent.Initializer.make(this)
    }

    override fun onCreate() {
        super.onCreate()

        Logger.log("onCreate: ${ProcessUtils.isMainProcess(this)}")

        STracker.appOnCreate()

        Stetho.initializeWithDefaults(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        initFlipper()
        BeagleInitializer.initialize(this)
    }

    private fun initFlipper() {
//        FlipperWrapper.setup(this)
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
