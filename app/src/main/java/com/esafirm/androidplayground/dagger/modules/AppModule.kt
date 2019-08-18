package com.esafirm.androidplayground.dagger.modules

import android.content.Context

import com.esafirm.androidplayground.dagger.example.NonScopedClass
import com.esafirm.androidplayground.dagger.example.ScopedClass

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context: Context) {

    @Provides
    internal fun provideContext(): Context {
        return context
    }

    @Provides
    internal fun someClassInAppComponent(): NonScopedClass {
        return NonScopedClass()
    }

    @Singleton
    @Provides
    internal fun singletonClass(): ScopedClass {
        return ScopedClass()
    }
}

