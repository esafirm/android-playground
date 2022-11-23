package com.esafirm.androidplayground.dagger

import com.esafirm.androidplayground.dagger.example.DaggerSampleController
import com.esafirm.androidplayground.dagger.modules.ControllerModule

import dagger.Component
import javax.inject.Named

@ControllerScope
@Component(
    modules = [ControllerModule::class],
    dependencies = [AppComponent::class, RandomStringProvider::class]
)
interface ControllerComponent {
    fun inject(daggerSampleController: DaggerSampleController)

    fun randomString(): String

    @Named("controller")
    fun controllerString(): String
}
