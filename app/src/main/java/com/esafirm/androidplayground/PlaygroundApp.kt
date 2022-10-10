package com.esafirm.androidplayground

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.esafirm.androidplayground.dagger.AppComponent
import com.esafirm.androidplayground.flipper.FlipperWrapper
import com.esafirm.androidplayground.startup.STracker
import com.esafirm.androidplayground.libs.Logger
import com.esafirm.androidplayground.utils.ProcessUtils
import com.facebook.stetho.Stetho
import com.gu.toolargetool.TooLargeTool

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
        TooLargeTool.startLogging(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                Log.d("ProcessLifecycle", "Event: $event")
            }
        })
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
