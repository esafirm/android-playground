package com.esafirm.androidplayground.dagger

import com.esafirm.androidplayground.dagger.example.DaggerSampleAct
import com.esafirm.androidplayground.dagger.modules.ActivityModule

import dagger.Component

@ActivityScope
@Component(modules = [ActivityModule::class], dependencies = [AppComponent::class])
interface ActivityComponent {
    fun inject(daggerSampleAct: DaggerSampleAct)
}
