package com.esafirm.androidplayground

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

import com.esafirm.androidplayground.dagger.AppComponent
import com.esafirm.androidplayground.startup.STracker
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
