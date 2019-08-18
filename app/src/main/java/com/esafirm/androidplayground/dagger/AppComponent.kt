package com.esafirm.androidplayground.dagger

import android.content.Context

import com.esafirm.androidplayground.App
import com.esafirm.androidplayground.dagger.example.NonScopedClass
import com.esafirm.androidplayground.dagger.example.ScopedClass
import com.esafirm.androidplayground.dagger.modules.AppModule

import javax.inject.Singleton

import dagger.Component

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun appContext(): Context
    fun classInAppComponent(): NonScopedClass
    fun singletonClass(): ScopedClass

    object Initializer {
        fun make(app: App): AppComponent {
            return DaggerAppComponent.builder()
                .appModule(AppModule(app))
                .build()
        }
    }
}
